use std::collections::HashMap;
use std::hash::Hash;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day5::new(read_input_lines(5)).run()
}

struct Day5 {
    lines: Vec<Line>,
}

struct Line {
    from: Position,
    to: Position,
}

#[derive(Hash, Eq, PartialEq)]
struct Position {
    x: usize,
    y: usize,
}

impl Day for Day5 {
    fn part1(&self) -> usize {
        Day5::count_occurrences(self.lines.iter().flat_map(|l| l.straight()).collect())
    }

    fn part2(&self) -> usize {
        Day5::count_occurrences(self.lines.iter().flat_map(|l| l.straight().into_iter().chain(l.diagonal().into_iter())).collect())
    }
}

impl Day5 {
    fn new(lines: Vec<String>) -> Day5 {
        Day5 { lines: lines.iter().filter_map(|l| l.split_once("->").map(|s| parse_line(s))).collect() }
    }

    fn count_occurrences(positions: Vec<Position>) -> usize {
        positions.into_iter()
            .fold(HashMap::<Position, usize>::new(), |mut m, x| {
                *m.entry(x).or_default() += 1;
                m
            })
            .into_iter()
            .filter(|(_, c)| *c >= 2)
            .count()
    }
}

impl Line {
    fn new(from: Position, to: Position) -> Line {
        Line { from, to }
    }

    fn straight(&self) -> Vec<Position> {
        let (x_equal, y_equal) = &self.to.equal(&self.from);
        if !x_equal ^ !y_equal {
            if !x_equal {
                return self.range(|p| p.x).into_iter().map(|x| Position::new(x, self.from.y)).collect();
            } else if !y_equal {
                return self.range(|p| p.y).into_iter().map(|y| Position::new(self.from.x, y)).collect();
            }
        }
        Vec::new()
    }

    fn diagonal(&self) -> Vec<Position> {
        let (x_equal, y_equal) = &self.to.equal(&self.from);
        if !x_equal && !y_equal && self.from.is_diagonal(&self.to) {
            return self.range(|p| p.x).into_iter().zip(self.range(|p| p.y)).map(|(x, y)| Position::new(x, y)).collect();
        }
        Vec::new()
    }

    fn range(&self, axis: fn(&Position) -> usize) -> Vec<usize> {
        if axis(&self.from) < axis(&self.to) {
            return (axis(&self.from)..=axis(&self.to)).collect();
        }
        return (axis(&self.to)..=axis(&self.from)).rev().collect();
    }
}

impl Position {
    fn new(x: usize, y: usize) -> Self {
        Position { x, y }
    }

    fn equal(&self, other: &Self) -> (bool, bool) {
        (self.x == other.x, self.y == other.y)
    }

    fn is_diagonal(&self, other: &Self) -> bool {
        (self.x as isize - other.x as isize).abs() == (self.y as isize - other.y as isize).abs()
    }
}

fn parse_line((to, from): (&str, &str)) -> Line {
    Line::new(parse_position(to), parse_position(from))
}

fn parse_position(raw: &str) -> Position {
    raw.trim().split_once(',')
        .map(|(x, y)| Position::new(x.parse::<usize>().unwrap(), y.parse::<usize>().unwrap()))
        .unwrap()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 5)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 12)
    }

    fn sample_day() -> Day5 {
        Day5::new(vec![
            String::from("0,9 -> 5,9"),
            String::from("8,0 -> 0,8"),
            String::from("9,4 -> 3,4"),
            String::from("2,2 -> 2,1"),
            String::from("7,0 -> 7,4"),
            String::from("6,4 -> 2,0"),
            String::from("0,9 -> 2,9"),
            String::from("3,4 -> 1,4"),
            String::from("0,0 -> 8,8"),
            String::from("5,5 -> 8,2"),
        ]
        )
    }
}
