use std::collections::HashMap;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day6::new(read_input_lines(6)).run()
}

struct Day6 {
    fish_count: HashMap<usize, usize>,
}

impl Day for Day6 {
    fn part1(&self) -> usize {
        total(self.fish_count.clone(), 80)
    }

    fn part2(&self) -> usize {
        total(self.fish_count.clone(), 256)
    }
}

impl Day6 {
    fn new(input: Vec<String>) -> Self {
        let numbers: Vec<usize> = input.join("").split(',').map(|s| s.parse::<usize>().unwrap()).collect();
        let fish_count = numbers.into_iter().fold(HashMap::new(), |mut m, i| {
            *m.entry(i).or_default() += 1;
            m
        });
        Day6 { fish_count }
    }
}

fn total(mut fish_count: HashMap<usize, usize>, remaining: usize) -> usize {
    let newborn = fish_count.get(&0).map(|i| *i).unwrap_or(0);
    let mut updated: HashMap<usize, usize> = fish_count.iter_mut().filter(|(k, _)| **k != 0).map(|(k, v)| (*k - 1, *v)).collect();
    if newborn > 0 {
        updated.insert(8, newborn);
        updated.entry(6).and_modify(|e| { *e += newborn }).or_insert(newborn);
    }
    return if remaining == 1 { updated.iter().map(|(_, v)| *v).sum() } else { total(updated, remaining - 1) };
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 5934)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 26984457539)
    }

    fn sample_day() -> Day6 {
        Day6::new(vec![3, 4, 3, 1, 2])
    }
}
