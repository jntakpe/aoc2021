use std::collections::HashMap;

use aoc_2021::shared::{Day, read_input_lines};

fn main() { Day11::new(read_input_lines(11)).run() }

struct Day11 {
    octopuses: HashMap<Position, Option<usize>>,
}

impl Day11 {
    fn new(input: Vec<String>) -> Day11 {
        let digits: HashMap<Position, Option<usize>> = input.iter()
            .map(|l| l.chars().map(|c| c.to_digit(10).unwrap() as usize))
            .enumerate()
            .flat_map(|(y, l)| l.enumerate().map(move |(x, d)| (Position { x, y }, Some(d))))
            .collect();
        Self { octopuses: digits }
    }
}

impl Day for Day11 {
    fn part1(&self) -> usize {
        let mut octopuses = self.octopuses.clone();
        (0..100).fold(0, |a, _| step(&mut octopuses) as usize + a)
    }

    fn part2(&self) -> usize {
        let mut octopuses = self.octopuses.clone();
        for i in 0..=i32::MAX {
            step(&mut octopuses);
            if octopuses.values().all(|v| *v == Some(0)) {
                return i as usize + 1;
            }
        }
        0
    }
}

#[derive(Clone, Copy, Hash, Eq, PartialEq)]
struct Position {
    x: usize,
    y: usize,
}

impl Position {
    fn new(x: usize, y: usize) -> Position {
        Self { x, y }
    }

    fn relative(&self, x: isize, y: isize) -> Option<Position> {
        let relative_x = self.x as isize - x;
        let relative_y = self.y as isize - y;
        if relative_x >= 0 && relative_y >= 0 {
            Some(Position::new(relative_x as usize, relative_y as usize))
        } else {
            None
        }
    }

    fn adjacent(&self) -> Vec<Position> {
        (-1..=1).flat_map(|x| (-1..=1).map(move |y| (x, y)))
            .filter_map(|(x, y)| self.relative(x, y))
            .filter(|p| p != self)
            .collect()
    }
}

fn step(octopuses: &mut HashMap<Position, Option<usize>>) -> usize {
    octopuses.values_mut().for_each(|v| *v = v.map(|l| l + 1));
    let count = octopuses.clone().keys()
        .fold(0, |a, c| { a + octopuses[c].filter(|l| *l == 10).map_or(0, |_| flash(octopuses, c)) });
    octopuses.values_mut().for_each(|v| { v.get_or_insert(0); });
    count
}

fn flash(octopuses: &mut HashMap<Position, Option<usize>>, position: &Position) -> usize {
    octopuses.insert(Position::new(position.x, position.y), None);
    return position.adjacent().iter().fold(1, |a, p| {
        octopuses.entry(Position::new(p.x, p.y)).and_modify(|l| *l = l.map(|v| v + 1));
        octopuses.get(p)
            .and_then(|v| v.filter(|l| *l >= 10))
            .map_or(a, |_| a + flash(octopuses, p))
    });
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 1656)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 195)
    }

    fn sample_day() -> Day11 {
        Day11::new(vec![
            String::from("5483143223"),
            String::from("2745854711"),
            String::from("5264556173"),
            String::from("6141336146"),
            String::from("6357385478"),
            String::from("4167524645"),
            String::from("2176841721"),
            String::from("6882881134"),
            String::from("4846848554"),
            String::from("5283751526"),
        ])
    }
}
