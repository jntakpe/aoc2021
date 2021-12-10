use std::collections::LinkedList;

use itertools::Itertools;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day10::new(read_input_lines(10)).run()
}

struct Day10 {
    errors: Vec<ParsingErrors>,
}

struct ParsingErrors {
    illegal_char: Option<char>,
    unclosed_chars: Option<LinkedList<char>>,
}

impl Day for Day10 {
    fn part1(&self) -> usize {
        self.errors.iter().map(|e| e.illegal_score()).sum()
    }

    fn part2(&self) -> usize {
        let scores: Vec<usize> = self.errors.iter().filter_map(|e| e.incomplete_score()).sorted().collect();
        scores[scores.len() / 2]
    }
}

impl Day10 {
    fn new(input: Vec<String>) -> Day10 {
        Day10 { errors: input.iter().filter_map(|l| parse_line(l)).collect() }
    }
}

impl ParsingErrors {
    fn new(illegal_char: Option<char>, unclosed_chars: Option<LinkedList<char>>) -> Option<ParsingErrors> {
        if illegal_char.is_some() {
            Some(ParsingErrors { illegal_char, unclosed_chars: None })
        } else {
            unclosed_chars
                .filter(|l| !l.is_empty())
                .map(|l| ParsingErrors { illegal_char: None, unclosed_chars: Some(l) })
        }
    }

    fn illegal_score(&self) -> usize {
        if let Some(char) = self.illegal_char {
            return match char {
                ')' => 3,
                ']' => 57,
                '}' => 1197,
                _ => 25137,
            };
        }
        0
    }

    fn incomplete_score(&self) -> Option<usize> {
        self.unclosed_chars.as_ref().map(|chars| chars.iter().map(|char| {
            match char {
                ')' => 1usize,
                ']' => 2,
                '}' => 3,
                _ => 4,
            }
        }).fold(0, |a, c| a * 5 + c))
    }
}

fn parse_line(line: &str) -> Option<ParsingErrors> {
    let mut open_stack = LinkedList::new();
    let char_tuples = vec![('[', ']'), ('(', ')'), ('{', '}'), ('<', '>')];
    for char in line.chars() {
        if char_tuples.iter().any(|(o, _)| *o == char) {
            open_stack.push_back(char);
        } else if open_stack.back().filter(|l| get_other_pair(&char_tuples, **l) == char).is_some() {
            open_stack.pop_back();
        } else {
            return ParsingErrors::new(Some(char), None);
        }
    }
    ParsingErrors::new(None, Some(open_stack.iter().rev().map(|c| get_other_pair(&char_tuples, *c)).collect()))
}

fn get_other_pair(char_tuples: &[(char, char)], char: char) -> char {
    char_tuples.iter()
        .find_map(|(o, c)| if *o == char { Some(*c) } else if *c == char { Some(*o) } else { None })
        .unwrap()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 26397)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 288957)
    }

    fn sample_day() -> Day10 {
        Day10::new(vec![
            String::from("[({(<(())[]>[[{[]{<()<>>"),
            String::from("[(()[<>])]({[<{<<[]>>("),
            String::from("{([(<{}[<>[]}>{[]{[(<()>"),
            String::from("(((({<>}<{<{<>}{[]{[]{}"),
            String::from("[[<[([]))<([[{}[[()]]]"),
            String::from("[{[{({}]{}}([{[{{{}}([]"),
            String::from("{<[[]]>}<{[{[{[]{()[[[]"),
            String::from("[<(<(<(<{}))><([]([]()"),
            String::from("<{([([[(<>()){}]>(<<{{"),
            String::from("<{([{{}}[<[[[<>{}]]]>[]]"),
        ])
    }
}
