use std::collections::HashMap;

use itertools::Itertools;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day9::new(read_input_lines(9)).run()
}

struct Day9 {
    digits: HashMap<Position, usize>,
}


#[derive(Hash, Eq, PartialEq)]
struct Position {
    x: usize,
    y: usize,
}

impl Day for Day9 {
    fn part1(&self) -> usize {
        self.low_points().iter().map(|(_, v)| v + 1).sum()
    }

    fn part2(&self) -> usize {
        self.low_points().into_iter()
            .map(|e| self.basin(HashMap::from([e])))
            .sorted()
            .rev()
            .take(3)
            .product()
    }
}

impl Day9 {
    fn new(input: Vec<String>) -> Day9 {
        let digits: HashMap<Position, usize> = input.iter()
            .map(|l| l.chars().map(|c| c.to_digit(10).unwrap() as usize))
            .enumerate()
            .flat_map(|(y, l)| l.enumerate().map(move |(x, d)| (Position { x, y }, d)))
            .collect();
        Day9 { digits }
    }

    fn low_points(&self) -> HashMap<&Position, usize> {
        self.digits.iter()
            .filter(|(pos, value)| {
                pos.adjacent().iter().filter_map(|p| self.digits.get(p)).all(|c| *c > **value)
            })
            .map(|(k, v)| (k, *v))
            .collect()
    }

    fn basin(&self, initial: HashMap<&Position, usize>) -> usize {
        let next: HashMap<&Position, usize> = initial.iter()
            .flat_map(|(p, v)| self.next_basin_cells(p, *v, &initial))
            .collect();
        let empty = next.is_empty();
        let all: HashMap<&Position, usize> = initial.into_iter().chain(next).collect();
        if !empty {
            return self.basin(all);
        }
        return all.values().count();
    }

    fn next_basin_cells(&self, position: &Position, value: usize, cells: &HashMap<&Position, usize>) -> Vec<(&Position, usize)> {
        position.adjacent().iter()
            .filter_map(|p| {
                self.digits.get_key_value(p)
                    .filter(|(_, v)| !cells.contains_key(&p) && **v > value && **v != 9)
                    .map(|(k, v)| (k, *v))
            })
            .collect()
    }
}

impl Position {
    fn new(x: usize, y: usize) -> Position {
        Position { x, y }
    }

    fn adjacent(&self) -> Vec<Position> {
        let mut positions = Vec::with_capacity(4);
        if self.x > 0 {
            positions.push(Position::new(self.x - 1, self.y));
        }
        if self.y > 0 {
            positions.push(Position::new(self.x, self.y - 1));
        }
        positions.push(Position::new(self.x + 1, self.y));
        positions.push(Position::new(self.x, self.y + 1));
        positions
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 15)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 1134)
    }

    fn sample_day() -> Day9 {
        Day9::new(vec![
            String::from("2199943210"),
            String::from("3987894921"),
            String::from("9856789892"),
            String::from("8767896789"),
            String::from("9899965678"),
        ])
    }
}
