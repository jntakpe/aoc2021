use std::ops::RangeInclusive;

use aoc_2021::shared::{Day, read_input_lines};

fn main() {
    Day17::new(read_input_lines(17)).run()
}

struct Day17 {
    position: Coordinates<i32>,
    target_area: Coordinates<RangeInclusive<i32>>,
}

struct Probe {
    position: Coordinates<i32>,
    target_area: Coordinates<RangeInclusive<i32>>,
}

#[derive(Clone)]
struct Coordinates<T> {
    x: T,
    y: T,
}

impl Probe {
    fn launch(&mut self, mut velocity: Coordinates<i32>) -> Option<i32> {
        let mut max = i32::MIN;
        while !self.within_target() {
            if self.target_missed() { return None; }
            self.position.step(&velocity);
            if max < self.position.y { max = self.position.y; }
            velocity.next();
        }
        Some(max)
    }

    fn within_target(&self) -> bool {
        self.target_area.x.contains(&self.position.x) && self.target_area.y.contains(&self.position.y)
    }

    fn target_missed(&self) -> bool {
        self.position.x > *self.target_area.x.end() || self.position.y < *self.target_area.y.start()
    }
}

impl<T> Coordinates<T> {
    fn new(x: T, y: T) -> Coordinates<T> {
        Coordinates { x, y }
    }
}

impl Coordinates<i32> {
    fn next(&mut self) {
        self.x = self.x - self.x.cmp(&0) as i32;
        self.y -= 1;
    }

    fn step(&mut self, velocity: &Self) {
        self.x += velocity.x;
        self.y += velocity.y;
    }
}

impl Day for Day17 {
    fn part1(&self) -> usize {
        self.coordinates().into_iter()
            .filter_map(|v| self.probe().launch(v))
            .max()
            .unwrap() as usize
    }

    fn part2(&self) -> usize {
        self.coordinates().into_iter()
            .filter_map(|v| self.probe().launch(v))
            .count()
    }
}

impl Day17 {
    fn new(raw: Vec<String>) -> Day17 {
        let (raw_x, raw_y) = raw.first().unwrap().split_once(',').unwrap();
        Day17 {
            position: Coordinates::new(0, 0),
            target_area: Coordinates::new(Self::range(raw_x, 'x'), Self::range(raw_y, 'y')),
        }
    }

    fn range(raw: &str, axis: char) -> RangeInclusive<i32> {
        let start = raw.find(format!("{}=", axis).as_str()).unwrap() + 2;
        let (from, to) = &raw[start..].split_once("..").unwrap();
        from.parse::<i32>().unwrap()..=to.parse::<i32>().unwrap()
    }

    fn probe(&self) -> Probe {
        Probe {
            position: self.position.clone(),
            target_area: self.target_area.clone(),
        }
    }

    fn coordinates(&self) -> Vec<Coordinates<i32>> {
        (0..=*self.target_area.x.end()).flat_map(|x| (*self.target_area.y.start()..=200).map(move |y| Coordinates::new(x, y))).collect()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn part1_sample() {
        assert_eq!(sample_day().part1(), 45);
    }

    #[test]
    fn part2_sample() {
        assert_eq!(sample_day().part2(), 112);
    }

    fn sample_day() -> Day17 {
        Day17::new(vec![String::from("target area: x=20..30, y=-10..-5")])
    }
}
