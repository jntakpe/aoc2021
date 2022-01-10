use std::collections::HashSet;
use std::ops::Range;

use itertools::Itertools;

use aoc_2021::shared::{Day, read_input_lines_preserving_empty};

fn main() {
    Day13::new(read_input_lines_preserving_empty(13)).run()
}

struct Day13 {
    grid: Grid,
}

struct Grid {
    positions: HashSet<Position>,
    folds: Vec<Fold>,
}

#[derive(Clone, Hash, PartialEq, Eq)]
struct Position {
    x: usize,
    y: usize,
}

#[derive(Clone)]
struct Fold {
    axis: Axis,
    value: usize,
}

#[derive(Clone)]
enum Axis {
    X,
    Y,
}

impl Day for Day13 {
    fn part1(&self) -> usize {
        self.grid.fold().unwrap().positions.len()
    }

    fn part2(&self) -> usize {
        self.grid.fold_all().display();
        0
    }
}

impl Day13 {
    fn new(lines: Vec<String>) -> Day13 {
        let (split_idx, _) = lines.iter().enumerate().find(|(_, s)| s.is_empty()).unwrap();
        let positions = Self::parse_positions(&lines, split_idx);
        let folds = Self::parse_folds(&lines, split_idx);
        Day13 { grid: Grid { positions, folds } }
    }

    fn parse_positions(lines: &[String], split_idx: usize) -> HashSet<Position> {
        Self::sub_vec(lines, 0..split_idx).iter()
            .filter_map(|s| s.split_once(","))
            .map(|(x, y)| Position::new(x.parse::<usize>().unwrap(), y.parse::<usize>().unwrap()))
            .collect()
    }

    fn parse_folds(lines: &[String], split_idx: usize) -> Vec<Fold> {
        Day13::sub_vec(lines, split_idx..lines.len()).iter()
            .filter_map(|s| s.split_once("="))
            .map(|(s, i)| Fold {
                axis: match s.chars().last().unwrap() {
                    'x' => Axis::X,
                    'y' => Axis::Y,
                    _ => panic!("Invalid char")
                },
                value: i.parse::<usize>().unwrap(),
            })
            .collect()
    }

    fn sub_vec(lines: &[String], range: Range<usize>) -> Vec<String> {
        lines[range].iter().filter(|l| !l.trim().is_empty()).map(String::from).collect()
    }
}

impl Position {
    fn new(x: usize, y: usize) -> Position {
        Position { x, y }
    }

    fn fold(&self, fold: &Fold) -> Position {
        match fold.axis {
            Axis::X => Position::new(fold.value * 2 - self.x, self.y),
            Axis::Y => Position::new(self.x, fold.value * 2 - self.y)
        }
    }

    fn axis(&self, axis: &Axis) -> usize {
        match axis {
            Axis::X => self.x,
            Axis::Y => self.y
        }
    }
}

impl Grid {
    fn fold_all(&self) -> Grid {
        let mut grid = self.fold().unwrap();
        while let Some(current) = grid.fold() {
            grid = current
        }
        grid
    }

    fn fold(&self) -> Option<Grid> {
        if let Some(fold) = self.folds.first() {
            let positions = self.positions.iter().filter(|p| p.axis(&fold.axis) < fold.value).cloned()
                .chain(self.positions.iter().filter(|p| p.axis(&fold.axis) > fold.value).map(|p| p.fold(fold)))
                .map(|p| Position::new(p.x, p.y))
                .collect();
            return Some(Grid { positions, folds: self.folds.clone().into_iter().skip(1).collect() });
        }
        None
    }

    fn display(&self) {
        let max_x = self.positions.iter().max_by_key(|p| p.x).unwrap().x;
        let max_y = self.positions.iter().max_by_key(|p| p.y).unwrap().y;
        (0..max_y).for_each(|y| {
            let line = (0..max_x)
                .map(move |x| Position::new(x, y))
                .map(|p| if self.positions.contains(&p) { '#' } else { '.' })
                .join("");
            println!("{}", line);
        });
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 17);
    }

    #[test]
    fn part2_sample() {
        sample_day().part2();
    }

    fn sample_day() -> Day13 {
        Day13::new(vec![
            String::from("6,10"),
            String::from("0,14"),
            String::from("9,10"),
            String::from("0,3"),
            String::from("10,4"),
            String::from("4,11"),
            String::from("6,0"),
            String::from("6,12"),
            String::from("4,1"),
            String::from("0,13"),
            String::from("10,12"),
            String::from("3,4"),
            String::from("3,0"),
            String::from("8,4"),
            String::from("1,10"),
            String::from("2,14"),
            String::from("8,10"),
            String::from("9,0"),
            String::from(""),
            String::from("fold along y=7"),
            String::from("fold along x=5"),
        ])
    }
}
