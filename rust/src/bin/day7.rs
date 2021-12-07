use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day7::new(read_input_lines(7)).run()
}

struct Day7 {
    numbers: Vec<usize>,
}

impl Day for Day7 {
    fn part1(&self) -> usize {
        self.resolve(gap)
    }

    fn part2(&self) -> usize {
        self.resolve(|from, dest| (0..=gap(from, dest)).sum())
    }
}

impl Day7 {
    fn new(input: Vec<String>) -> Day7 {
        Day7 { numbers: input.join("").split(',').map(|s| s.parse::<usize>().unwrap()).collect() }
    }

    fn resolve(&self, fuel: fn(usize, usize) -> usize) -> usize {
        let min = *self.numbers.iter().min().unwrap();
        let max = *self.numbers.iter().max().unwrap();
        (min..max).map(|dest| self.numbers.iter().map(|from| fuel(*from, dest)).sum()).min().unwrap()
    }
}

fn gap(from: usize, dest: usize) -> usize {
    (from as isize - dest as isize).abs() as usize
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 37)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 168)
    }

    fn sample_day() -> Day7 {
        Day7::new(vec![String::from("16,1,2,0,4,2,7,1,2,14")])
    }
}
