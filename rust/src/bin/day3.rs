use std::ops::Range;

use itertools::Itertools;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day3 {
        input: read_input_lines(3)
    }.run()
}

struct Day3 {
    input: Vec<String>,
}

impl Day for Day3 {
    fn part1(&self) -> usize {
        let binary = range(&self.input).map(|i| common_bit(&self.input, i)).join("");
        decimal(binary.as_str()) * decimal(reverse_bits(binary.as_str()).as_str())
    }

    fn part2(&self) -> usize {
        todo!()
    }
}

fn common_bit(bits: &Vec<String>, index: usize) -> char {
    let count = bits.iter().filter_map(|s| s.chars().nth(index)).filter(|c| *c == '0').count();
    if count > bits.len() / 2 {
        '0'
    } else {
        '1'
    }
}

fn reverse_bits(bits: &str) -> String {
    bits.chars().map(|c| reverse(c)).join("")
}

fn reverse(bit: char) -> char {
    if bit == '0' { '1' } else { '0' }
}

fn decimal(bits: &str) -> usize {
    usize::from_str_radix(bits, 2).unwrap()
}

fn range(input: &Vec<String>) -> Range<usize> {
    (0..input.first().unwrap().len())
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 198)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 230)
    }

    fn sample_day() -> Day3 {
        Day3 {
            input: vec![
                String::from("00100"),
                String::from("11110"),
                String::from("10110"),
                String::from("10111"),
                String::from("10101"),
                String::from("01111"),
                String::from("00111"),
                String::from("11100"),
                String::from("10000"),
                String::from("11001"),
                String::from("00010"),
                String::from("01010"),
            ]
        }
    }
}
