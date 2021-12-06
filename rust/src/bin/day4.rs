use itertools::Itertools;

use aoc_2021::shared::{Day, read_input_lines_preserving_empty};

fn main() {
    Day4::new(read_input_lines_preserving_empty(4)).run()
}

struct Day4 {
    numbers: Vec<usize>,
    boards: Vec<Board>,
}

#[derive(Clone)]
struct Board {
    cells: Vec<Cell>,
    resolved: bool,
}

#[derive(Clone)]
struct Cell {
    position: Position,
    value: usize,
    called: bool,
}

#[derive(Clone, PartialEq)]
struct Position {
    x: usize,
    y: usize,
}

impl Day for Day4 {
    fn part1(&self) -> usize {
        let mut boards = self.boards.clone();
        for number in &self.numbers {
            for board in &mut boards {
                if board.complete(*number) {
                    return *number * board.result();
                }
            }
        }
        panic!("No result found")
    }

    fn part2(&self) -> usize {
        todo!()
    }
}

impl Day4 {
    fn new(lines: Vec<String>) -> Self {
        Day4 {
            numbers: parse_numbers(&lines),
            boards: parse_boards(lines.into_iter().skip(1).collect()),
        }
    }
}

impl Board {
    fn new(cells: Vec<Cell>) -> Self {
        Board { cells, resolved: false }
    }

    fn resolve(&mut self) {
        self.resolved = true
    }

    fn result(&self) -> usize {
        self.cells.iter().filter(|c| !c.called).map(|c| c.value).sum()
    }

    fn complete(&mut self, number: usize) -> bool {
        if self.resolved {
            return false;
        }
        let cells = self.cells.clone();
        if let Some(cell) = self.cells.iter_mut().find(|c| c.value == number) {
            if Board::call(&cells, cell) {
                self.resolve();
            }
            return self.resolved;
        }
        return false;
    }

    fn call(cells: &Vec<Cell>, cell: &mut Cell) -> bool {
        cell.call();
        Board::all_complete(cells, &cell, |c| c.position.x) || Board::all_complete(cells, &cell, |c| c.position.y)
    }

    fn all_complete(cells: &Vec<Cell>, cell: &Cell, extract: fn(&Cell) -> usize) -> bool {
        cells.iter().filter(|c| c.position != cell.position && extract(c) == extract(cell)).all(|c| c.called)
    }
}

impl Cell {
    fn new(x: usize, y: usize, value: usize) -> Self {
        Cell { position: Position { x, y }, value, called: false }
    }

    fn call(&mut self) {
        self.called = true
    }
}

fn parse_numbers(lines: &Vec<String>) -> Vec<usize> {
    lines.first().map_or(Vec::new(), |i| i.split(',').map(|s| s.parse::<usize>().unwrap()).collect())
}

fn parse_boards(lines: Vec<String>) -> Vec<Board> {
    let board_lines: Vec<String> = lines.into_iter().skip(1).collect();
    let i = board_size(&board_lines);
    board_lines.chunks(i).map(|l| Board::new(parse_cells(l))).collect()
}

fn board_size(lines: &Vec<String>) -> usize {
    lines.iter().enumerate().find_or_first(|(_, l)| l.trim().is_empty()).map(|(i, _)| i).unwrap() + 1
}

fn parse_cells(lines: &[String]) -> Vec<Cell> {
    lines.iter().enumerate().flat_map(|(i, l)| parse_line(i, l.as_str())).collect()
}

fn parse_line(index: usize, line: &str) -> Vec<Cell> {
    line.split_whitespace()
        .filter(|s| !s.trim().is_empty())
        .enumerate()
        .map(|(i, s)| Cell::new(i, index, s.parse::<usize>().unwrap()))
        .collect()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 4512)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 1924)
    }

    fn sample_day() -> Day4 {
        Day4::new(vec![
            String::from("7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1"),
            String::from(""),
            String::from("22 13 17 11  0"),
            String::from(" 8  2 23  4 24"),
            String::from("21  9 14 16  7"),
            String::from(" 6 10  3 18  5"),
            String::from(" 1 12 20 15 19"),
            String::from(""),
            String::from(" 3 15  0  2 22"),
            String::from(" 9 18 13 17  5"),
            String::from("19  8  7 25 23"),
            String::from("20 11 10 24  4"),
            String::from("14 21 16 12  6"),
            String::from(""),
            String::from("14 21 17 24  4"),
            String::from("10 16 15  9 19"),
            String::from("18  8 23 26 20"),
            String::from("22 11 13  6  5"),
            String::from(" 2  0 12  3  7"),
        ]
        )
    }
}
