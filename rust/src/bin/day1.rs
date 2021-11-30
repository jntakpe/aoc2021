use aoc_2021::shared::Day;

fn main() {
    todo!()
}

struct Day1 {
    input: Vec<String>
}

impl Day for Day1 {
    fn part1(&self) -> usize {
        todo!()
    }

    fn part2(&self) -> usize {
        todo!()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 0)
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 0)
    }

    fn sample_day() -> Day1 {
        Day1 {
            input: vec![String::from("foo")]
        }
    }
}
