use std::collections::HashMap;

use itertools::Itertools;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day14::new(read_input_lines(14)).run();
}

struct Day14 {
    frequency: HashMap<String, usize>,
    rules: HashMap<String, Vec<String>>,
    last_char: char,
}

impl Day for Day14 {
    fn part1(&self) -> usize {
        self.result(10)
    }

    fn part2(&self) -> usize {
        self.result(40)
    }
}

impl Day14 {
    fn new(lines: Vec<String>) -> Day14 {
        Day14 {
            frequency: Self::parse_frequency(lines.first().unwrap()),
            rules: lines.iter().skip(1).filter_map(|l| Self::parse_rules(l)).collect(),
            last_char: lines.first().unwrap().chars().last().unwrap(),
        }
    }

    fn parse_frequency(template: &str) -> HashMap<String, usize> {
        return template
            .chars()
            .tuple_windows()
            .map(|(a, b)| format!("{}{}", a, b))
            .fold(HashMap::<String, usize>::new(), |mut m, s| {
                *m.entry(s).or_default() += 1;
                m
            })
            .into_iter()
            .collect();
    }

    fn parse_rules(rule: &str) -> Option<(String, Vec<String>)> {
        return rule.split_once(" -> ")
            .map(|(s, c)| (s.to_string(), vec![format!("{}{}", &s[..1], c), format!("{}{}", c, &s[1..])]));
    }

    fn result(&self, steps: usize) -> usize {
        let char_freq = self.after_step(steps).iter().fold(HashMap::from([(self.last_char, 1)]), |mut a, (rule, freq)| {
            a.entry(rule.chars().next().unwrap()).and_modify(|f| { *f += freq }).or_insert(*freq);
            a
        });
        return char_freq.values().max().unwrap() - char_freq.values().min().unwrap()
    }

    fn after_step(&self, steps: usize) -> HashMap<String, usize> {
        (0..steps).fold(self.frequency.clone(), |a, _| self.next(a))
    }

    fn next(&self, frequency: HashMap<String, usize>) -> HashMap<String, usize> {
        frequency.iter().fold(HashMap::<String, usize>::new(), |mut a, (rule, freq)| {
            self.rules[rule].iter().for_each(|r| { a.entry(r.clone()).and_modify(|f| { *f += freq }).or_insert(*freq); });
            a
        })
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 1588);
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 2188189693529);
    }

    fn sample_day() -> Day14 {
        Day14::new(vec![
            String::from("NNCB"),
            String::from(""),
            String::from("CH -> B"),
            String::from("HH -> N"),
            String::from("CB -> H"),
            String::from("NH -> C"),
            String::from("HB -> C"),
            String::from("HC -> B"),
            String::from("HN -> C"),
            String::from("NN -> C"),
            String::from("BH -> H"),
            String::from("NC -> B"),
            String::from("NB -> B"),
            String::from("BN -> B"),
            String::from("BB -> N"),
            String::from("BC -> B"),
            String::from("CC -> N"),
            String::from("CN -> C"),
        ])
    }
}
