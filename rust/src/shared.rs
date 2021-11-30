use std::fs::File;
use std::io::{BufRead, BufReader};

pub fn read_input_numbers(day: usize) -> Vec<usize> {
    read_input_lines(day)
        .iter()
        .filter_map(|i| i.parse().ok())
        .collect()
}

pub fn read_input_lines(day: usize) -> Vec<String> {
    read_lines(day, false)
}

pub fn read_input_lines_preserving_empty(day: usize) -> Vec<String> {
    read_lines(day, true)
}

fn read_lines(day: usize, preserve_empty: bool) -> Vec<String> {
    BufReader::new(open_file(day))
        .lines()
        .filter_map(Result::ok)
        .filter(|s| preserve_empty || !s.trim().is_empty())
        .collect()
}

fn open_file(day: usize) -> File {
    let filename = format!("inputs/day_{}.txt", day);
    File::open(&filename).expect(format!("Unable to open file: '{}'", &filename).as_str())
}

pub trait Day {
    fn part1(&self) -> usize;

    fn part2(&self) -> usize;

    fn run(&self) {
        println!("Part 1: {}", self.part1());
        println!("Part 2: {}", self.part2());
    }
}
