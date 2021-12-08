use std::collections::HashMap;

use itertools::Itertools;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day8::new(read_input_lines(8)).run()
}

struct Day8 {
    input: Vec<(Vec<String>, Vec<String>)>,
}

impl Day for Day8 {
    fn part1(&self) -> usize {
        self.input.iter().map(|(_, d)| count_guessable(d)).sum()
    }

    fn part2(&self) -> usize {
        self.input.iter().map(|(p, d)| sum_line(p, d)).sum()
    }
}

impl Day8 {
    fn new(raw: Vec<String>) -> Day8 {
        Day8 {
            input: raw.iter().map(|l| Day8::parse_line(l.as_str())).collect()
        }
    }

    fn parse_line(line: &str) -> (Vec<String>, Vec<String>) {
        line.split('|')
            .map(|s| s.trim())
            .map(|p| p.split_whitespace().map(|s| String::from(s)).collect())
            .collect_tuple()
            .unwrap()
    }
}

fn count_guessable(words: &Vec<String>) -> usize {
    words.iter().filter(|d| vec![2, 3, 4, 7].contains(&d.len())).count()
}

fn sum_line(patterns: &Vec<String>, digits: &Vec<String>) -> usize {
    let lengths = pattern_length(&patterns);
    let digit_map = digit_map(&lengths);
    digits.iter()
        .map(|d| digit_map.iter().find(|(_, v)| is_same(v, d)).unwrap())
        .map(|(k, _)| k.to_string())
        .join("")
        .parse::<usize>()
        .unwrap()
}

fn pattern_length(raw_patterns: &Vec<String>) -> HashMap<usize, Vec<String>> {
    let mut pattern_length: HashMap<usize, Vec<String>> = HashMap::with_capacity(6);

    for (key, group) in &raw_patterns.iter().group_by(|elt| elt.len()) {
        let patterns: Vec<String> = group.map(|s| String::from(s)).collect();
        pattern_length.entry(key)
            .and_modify(|e| e.append(&mut patterns.clone()))
            .or_insert(patterns);
    }
    pattern_length
}

fn digit_map(pattern_length: &HashMap<usize, Vec<String>>) -> HashMap<usize, String> {
    let mut digit_map: HashMap<usize, String> = HashMap::with_capacity(10);
    digit_map.insert(1, single_value(pattern_length, 2));
    digit_map.insert(7, single_value(pattern_length, 3));
    digit_map.insert(4, single_value(pattern_length, 4));
    digit_map.insert(8, single_value(pattern_length, 7));
    multi_values(pattern_length, 6).iter().for_each(|w| {
        if !contains_all(w, digit_map.get(&1).unwrap()) {
            digit_map.insert(6, String::from(w));
        } else if contains_all(w, digit_map.get(&4).unwrap()) {
            digit_map.insert(9, String::from(w));
        } else {
            digit_map.insert(0, String::from(w));
        }
    });
    multi_values(pattern_length, 5).iter().for_each(|w| {
        if contains_all(w, digit_map.get(&1).unwrap()) {
            digit_map.insert(3, String::from(w));
        } else if contains_all(digit_map.get(&9).unwrap(), w) {
            digit_map.insert(5, String::from(w));
        } else {
            digit_map.insert(2, String::from(w));
        }
    });
    digit_map
}

fn single_value(map: &HashMap<usize, Vec<String>>, key: usize) -> String {
    map[&key].first().map(|w| String::from(w)).unwrap()
}

fn multi_values(map: &HashMap<usize, Vec<String>>, key: usize) -> Vec<String> {
    map[&key].iter().map(|w| String::from(w)).collect()
}

fn contains_all(digit: &str, other: &str) -> bool {
    other.chars().all(|c| digit.contains(c))
}

fn is_same(pattern: &str, other: &str) -> bool {
    pattern.len() == other.len() && contains_all(pattern, other)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 26)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 61229)
    }

    fn sample_day() -> Day8 {
        Day8::new(vec![
            String::from("be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"),
            String::from("edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc"),
            String::from("fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg"),
            String::from("fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb"),
            String::from("aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea"),
            String::from("fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb"),
            String::from("dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe"),
            String::from("bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef"),
            String::from("egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb"),
            String::from("gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"),
        ])
    }
}
