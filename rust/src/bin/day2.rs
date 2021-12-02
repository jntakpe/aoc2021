use aoc_2021::shared::{Day, read_input_lines};

use crate::Move::{Down, Forward, Up};

fn main() {
    Day2 {
        input: read_input_lines(2)
    }.run()
}

struct Day2 {
    input: Vec<String>,
}

enum Move {
    Forward(isize),
    Up(isize),
    Down(isize),
}

struct Position {
    horizontal: isize,
    depth: isize,
    aim: isize,
}

impl Position {
    fn new() -> Position {
        Position { horizontal: 0, depth: 0, aim: 0 }
    }

    fn shift(&mut self, shift: &Move) {
        match shift {
            Forward(u) => self.horizontal += u,
            Up(u) => self.depth += u,
            Down(u) => self.depth -= u
        }
    }

    fn aim(&mut self, aim: &Move) {
        match aim {
            Forward(u) => {
                self.horizontal += u;
                self.depth += self.aim * u
            }
            Up(u) => self.aim -= u,
            Down(u) => self.aim += u
        }
    }

    fn result(&self) -> usize {
        (&self.horizontal * &self.depth.abs()) as usize
    }
}

fn parse(raw: &str) -> Move {
    let split: Vec<&str> = raw.split(" ").collect();
    let (direction, unit) = (split[0], split[1].parse::<isize>().unwrap());
    match direction {
        "forward" => Forward(unit),
        "up" => Up(unit),
        "down" => Down(unit),
        _ => panic!("Unknown direction {}", direction)
    }
}

impl Day for Day2 {
    fn part1(&self) -> usize {
        let moves: Vec<Move> = self.input.iter().map(|s| parse(s.as_str())).collect();
        let position = moves.iter().fold(Position::new(), |mut acc, cur| {
            acc.shift(cur);
            acc
        });
        position.result()
    }

    fn part2(&self) -> usize {
        let moves: Vec<Move> = self.input.iter().map(|s| parse(s.as_str())).collect();
        let position = moves.iter().fold(Position::new(), |mut acc, cur| {
            acc.aim(cur);
            acc
        });
        position.result()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 150)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 900)
    }

    fn sample_day() -> Day2 {
        Day2 {
            input: vec![
                String::from("forward 5"),
                String::from("down 5"),
                String::from("forward 8"),
                String::from("up 3"),
                String::from("down 8"),
                String::from("forward 2"),
            ]
        }
    }
}
