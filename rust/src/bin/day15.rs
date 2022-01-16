use std::cmp::Ordering;
use std::collections::{BinaryHeap, HashMap};

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day15::new(read_input_lines(15)).run()
}

struct Day15 {
    //TODO add adjacent
    normal: Ceiling,
    scaled: Ceiling,
}

#[derive(Clone, Hash, Eq, PartialEq, Ord, PartialOrd)]
struct Position {
    x: usize,
    y: usize,
}

#[derive(Eq, PartialEq)]
struct Node<'a> {
    position: &'a Position,
    risk: u32,
}

struct Ceiling {
    map: HashMap<Position, u32>,
    end: Position,
}

impl Day for Day15 {
    fn part1(&self) -> usize {
        self.normal.shortest_path()
    }

    fn part2(&self) -> usize {
        self.scaled.shortest_path()
    }
}

impl Day15 {
    fn new(lines: Vec<String>) -> Day15 {
        let map: HashMap<Position, u32> = lines.iter().enumerate()
            .flat_map(|(y, s)| s.chars().enumerate().map(move |(x, c)| (Position { x, y }, c.to_digit(10).unwrap())))
            .collect();
        Day15 {
            normal: Ceiling::new(&map, 0),
            scaled: Ceiling::new(&map, 5),
        }
    }
}

impl Ceiling {
    fn new(map: &HashMap<Position, u32>, scale: u32) -> Ceiling {
        let map = Self::scale(map, scale);
        let end = map.keys().max().unwrap().clone();
        Ceiling { map, end }
    }

    fn scale(map: &HashMap<Position, u32>, scale: u32) -> HashMap<Position, u32> {
        let end = map.keys().max().unwrap().clone();
        let x_scaled = Self::scale_axis(map, scale, end.x, Position::scale_x);
        Self::scale_axis(&x_scaled, scale, end.y, Position::scale_y)
    }

    fn scale_axis(map: &HashMap<Position, u32>, scale: u32, max: usize, axis: fn(&Position, usize, usize) -> Position) -> HashMap<Position, u32> {
        (1..scale).fold(map.clone(), |mut a, c| {
            map.iter().for_each(|(k, v)| {
                let position = axis(k, max, c as usize);
                let next = (c + v - 1) % 9 + 1;
                a.insert(position, next);
            });
            a
        })
    }

    fn shortest_path(&self) -> usize {
        let mut heap = BinaryHeap::new();
        let start = Position::new(0, 0);
        heap.push(Node::new(&start, 0));
        let mut risks: HashMap<&Position, u32> = self.map.iter()
            .map(|(k, _)| (k, if k == &start { 0 } else { u32::MAX }))
            .collect();
        while let Some(Node { position, risk }) = heap.pop() {
            if position == &self.end { return risks[position] as usize; }
            if risks[&position] < risk { continue; }
            position.adjacent().iter().filter_map(|p| self.map.get_key_value(p)).for_each(|(k, v)| {
                let risk = risk + v;
                if risk < risks[k] {
                    risks.entry(k).and_modify(|r| *r = risk);
                    heap.push(Node::new(k, risk));
                }
            })
        }
        panic!("No result");
    }
}

impl Position {
    fn new(x: usize, y: usize) -> Position {
        Position { x, y }
    }

    fn adjacent(&self) -> Vec<Position> {
        let mut positions = vec![Position::new(self.x + 1, self.y), Position::new(self.x, self.y + 1)];
        if self.x > 0 { positions.push(Position::new(self.x - 1, self.y)); }
        if self.y > 0 { positions.push(Position::new(self.x, self.y - 1)); }
        positions
    }

    fn scale_x(&self, size: usize, times: usize) -> Position {
        Position::new(self.x + (size + 1) * times, self.y)
    }

    fn scale_y(&self, size: usize, times: usize) -> Position {
        Position::new(self.x, self.y + (size + 1) * times)
    }
}

impl<'a> Node<'_> {
    fn new(position: &Position, risk: u32) -> Node {
        Node { position, risk }
    }
}

impl Ord for Node<'_> {
    fn cmp(&self, other: &Self) -> Ordering {
        other.risk.cmp(&self.risk).then_with(|| self.position.cmp(other.position))
    }
}

impl PartialOrd for Node<'_> {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 40);
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 315);
    }

    fn sample_day() -> Day15 {
        Day15::new(vec![
            String::from("1163751742"),
            String::from("1381373672"),
            String::from("2136511328"),
            String::from("3694931569"),
            String::from("7463417111"),
            String::from("1319128137"),
            String::from("1359912421"),
            String::from("3125421639"),
            String::from("1293138521"),
            String::from("2311944581"),
        ])
    }
}
