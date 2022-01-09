use itertools::Itertools;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day12::new(read_input_lines(12)).run()
}

struct Day12 {
    paths: Vec<(String, String)>,
}

impl Day for Day12 {
    fn part1(&self) -> usize {
        self.visit(
            "start",
            &Vec::new(),
            |cave, visited, _| cave.to_uppercase() == *cave || !visited.contains(cave),
            false,
        )
    }

    fn part2(&self) -> usize {
        self.visit(
            "start",
            &Vec::new(),
            |cave, visited, wildcard| {
                cave != "start" && wildcard || cave.to_uppercase() == *cave || !visited.contains(cave)
            },
            true,
        )
    }
}

impl Day12 {
    fn new(lines: Vec<String>) -> Day12 {
        Day12 { paths: lines.iter().flat_map(|l| parse(l.as_str())).collect() }
    }

    fn visit(&self, cave: &str, visited: &Vec<String>, not_visited: fn(&String, &Vec<String>, bool) -> bool, wildcard: bool) -> usize {
        let all: Vec<String> = visited.iter().map(|s| String::from(s)).chain(vec![String::from(cave)]).collect();
        if cave == "end" { return 1; }
        self.paths.iter()
            .filter(|(start, end)| cave == start && not_visited(end, &all, wildcard))
            .fold(0, |a, (_, end)| {
                let wildcard = if not_visited(end, &all, false) { wildcard } else { false };
                a + self.visit(end.as_str(), &all, not_visited, wildcard)
            })
    }
}

fn parse(line: &str) -> Vec<(String, String)> {
    return line.split("-")
        .map(|s| String::from(s))
        .permutations(2)
        .map(|l| l.iter().map(|s| String::from(s)).collect_tuple().unwrap())
        .collect();
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day_short().part1(), 19);
        assert_eq!(sample_day_large().part1(), 226);
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day_short().part2(), 103);
        assert_eq!(sample_day_large().part2(), 3509);
    }

    fn sample_day_short() -> Day12 {
        Day12::new(vec![
            String::from("dc-end"),
            String::from("HN-start"),
            String::from("start-kj"),
            String::from("dc-start"),
            String::from("dc-HN"),
            String::from("LN-dc"),
            String::from("HN-end"),
            String::from("kj-sa"),
            String::from("kj-HN"),
            String::from("kj-dc")]
        )
    }

    fn sample_day_large() -> Day12 {
        Day12::new(vec![
            String::from("fs-end"),
            String::from("he-DX"),
            String::from("fs-he"),
            String::from("start-DX"),
            String::from("pj-DX"),
            String::from("end-zg"),
            String::from("zg-sl"),
            String::from("zg-pj"),
            String::from("pj-he"),
            String::from("RW-he"),
            String::from("fs-DX"),
            String::from("pj-RW"),
            String::from("zg-RW"),
            String::from("start-pj"),
            String::from("he-WI"),
            String::from("zg-he"),
            String::from("pj-fs"),
            String::from("start-RW")]
        )
    }
}
