use aoc_2021::shared::{Day, read_input_numbers};

fn main() {
    Day1 {
        input: read_input_numbers(1)
    }.run()
}

struct Day1 {
    input: Vec<usize>,
}

impl Day for Day1 {
    fn part1(&self) -> usize {
        self.input.windows(2).filter(|p| p[0] < p[1]).count()
    }

    fn part2(&self) -> usize {
        let sums: Vec<usize> = self.input.windows(3).map(|w| w.iter().sum()).collect();
        sums.windows(2).filter(|p| p[0] < p[1]).count()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 7)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 5)
    }

    fn sample_day() -> Day1 {
        Day1 {
            input: vec![199, 200, 208, 210, 200, 207, 240, 269, 260, 263]
        }
    }
}
