import com.github.jntakpe.aoc2021.shared.Day
import com.github.jntakpe.aoc2021.shared.readInputSplitOnBlank
import java.util.*
import kotlin.math.abs

object Day23 : Day {

    private val exits = listOf(2, 4, 6, 8)
    override val input = readInputSplitOnBlank(23)

    override fun part1() = sort(parseInput(0), 2)

    override fun part2() = sort(parseInput(1), 4)

    enum class Pod(val cost: Int) {
        A(1),
        B(10),
        C(100),
        D(1000)
    }

    private fun parseInput(problem: Int): List<List<Pod>> {
        return input[problem].lines()
            .map { l -> l.filter { c -> c in Pod.values().map { it.toString().single() } } }
            .filter { it.isNotEmpty() }
            .map { s -> s.map { Pod.valueOf(it.toString()) } }
            .run { (0 until first().size).map { i -> map { it[i] } } }
            .map { it.reversed() }
    }

    private fun sort(pods: List<List<Pod>>, roomSize: Int): Int {
        val queue = PriorityQueue<State>().apply { add(State(0, Setup(pods))) }
        val visited = mutableSetOf<Setup>()
        while (queue.isNotEmpty()) {
            with(queue.poll()) {
                if (setup.isSorted(roomSize)) return energy
                if (setup in visited) return@with
                setup.moveToHallway(roomSize, energy, visited, queue)
                setup.moveToRoom(roomSize, energy, visited, queue)
            }
        }
        error("No result")
    }

    private fun Setup.moveToHallway(roomSize: Int, energy: Int, visited: MutableSet<Setup>, queue: PriorityQueue<State>) {
        visited.add(this)
        pods.asSequence()
            .withIndex()
            .filter { (i, v) -> v.isNotEmpty() && !v.sameRoom(i) }
            .forEach { (i, v) ->
                val pod = v.last()
                listOf((exits[i] downTo 0) - exits, (exits[i]..10) - exits)
                    .forEach { side ->
                        side.takeWhile { isHallwayFree(it) }
                            .map { it to Setup(pods.edit(i, v.dropLast(1)), hallway.edit(it, pod.ordinal)) }
                            .filter { (_, setup) -> setup !in visited }
                            .map { (pos, setup) -> State(energy + (roomSize - v.size + abs(exits[i] - pos) + 1) * pod.cost, setup) }
                            .forEach { queue.add(it) }
                    }
            }
    }

    private fun Setup.moveToRoom(roomSize: Int, energy: Int, visited: Set<Setup>, queue: PriorityQueue<State>) {
        hallway.asSequence()
            .withIndex()
            .filter { (i, pod) -> !roomBlocked(i, pod) }
            .map { (i, pod) -> Triple(Setup(pods.edit(pod!!, pods[pod] + Pod.values()[pod]), hallway.edit(i, null)), i, pod) }
            .filter { (setup) -> setup !in visited }
            .map { (setup, i, pod) -> State(energy + (roomSize - occupants(pod) + abs(exits[pod] - i)) * Pod.values()[pod].cost, setup) }
            .toList()
            .forEach { queue.add(it) }
    }

    private fun Setup.roomBlocked(index: Int, pod: Int?): Boolean {
        return isHallwayFree(index) ||
                index < exits[pod!!] && !isHallwayFree(index + 1 until exits[pod]) ||
                index > exits[pod] && !isHallwayFree(exits[pod] + 1 until index) ||
                pods[pod].any { it.ordinal != pod }
    }

    data class State(val energy: Int, val setup: Setup) : Comparable<State> {

        override fun compareTo(other: State) = energy.compareTo(other.energy)
    }

    data class Setup(val pods: List<List<Pod>>, val hallway: List<Int?> = List(11) { null }) {

        fun isSorted(size: Int) = hallway.all { it == null } && pods.withIndex().all { (i, v) -> v.size == size && v.sameRoom(i) }

        fun isHallwayFree(index: Int) = hallway[index] == null

        fun isHallwayFree(range: IntRange) = range.all { hallway[it] == null }

        fun occupants(room: Int) = pods[room].size
    }

    private fun List<Pod>.sameRoom(number: Int) = isNotEmpty() && all { it.ordinal == number }

    private fun <T> List<T>.edit(index: Int, value: T) = toMutableList().apply { set(index, value) }
}
