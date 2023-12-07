package y23

import join
import puzzle

fun main() = puzzle(2023, 7) {
    val hands = inputLines.map { line ->
        val (cards, bid) = line.split(" ")
        cards.toCharArray().toList() to bid.toInt()
    }

    submit {
        hands.map { (cards, bid) -> Hand(cards) to bid }.sortedBy(Pair<Hand, Int>::first).foldIndexed(0) { i, acc, (hand, bid) ->
            bid * (i + 1) + acc
        }
    }

    submit {
        hands.map { (cards, bid) -> JokerHand(cards) to bid }.sortedBy(Pair<JokerHand, Int>::first).foldIndexed(0) { i, acc, (hand, bid) ->
            bid * (i + 1) + acc
        }
    }
}


private open class Hand(val cards: List<Char>): Comparable<Hand> {
    open val cardValues = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
    open fun value(): Int {
        val grouped = cards.groupingBy { it }.eachCount()

        val rank = if (grouped.containsValue(5)) {
            7 // Five of a kind
        } else if (grouped.containsValue(4)) {
            6 // Four of a kind
        } else if (grouped.containsValue(3) && grouped.containsValue(2)) {
            5 // Full house
        } else if (grouped.containsValue(3)) {
            4 // Three of a kind
        } else if (grouped.count { it.value == 2 } == 2) {
            3 // Two pair
        } else if (grouped.containsValue(2)) {
            2 // One pair
        } else {
            1 // High card
        }

        return rank
    }

    override operator fun compareTo(other: Hand): Int {
        val rank = value()
        val otherRank = other.value()
        if (rank != otherRank) {
            return rank.compareTo(otherRank)
        }

        return cards.zip(other.cards).first { (a, b) -> a != b }.let { (a, b) ->
            cardValues.indexOf(a).compareTo(cardValues.indexOf(b))
        }
    }
}

private class JokerHand(cards: List<Char>): Hand(cards) {
    override val cardValues = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

    override fun value(): Int {
        val jokers = cards.count { it == 'J' }
        val grouped = cards.filterNot { it == 'J' }.groupingBy { it }.eachCount()


        return when {
            grouped.containsValue(5) || grouped.containsValue(5-jokers) || grouped.isEmpty() -> 7 // Five of a kind. Either only 1 type of card or only jokers. Guaranteed with 4 or more jokers
            grouped.containsValue(4) || grouped.containsValue(4-jokers) -> 6 // Four of a kind. Guaranteed with 3 or more jokers
            (grouped.containsValue(3) && grouped.containsValue(2)) || (jokers == 2 && grouped.containsValue(2)) || (jokers == 1 && (grouped.containsValue(3) || grouped.count { it.value == 2 } == 2)) -> 5 // Full house. Guaranteed with 2 or more jokers
            grouped.containsValue(3) || grouped.containsValue(3-jokers) -> 4 // Three of a kind. Guaranteed with 2 or more jokers
            grouped.count { it.value == 2 } == 2 || (grouped.any { it.value == 2 } && jokers >= 1) -> 3 // Two pair. Guaranteed with 1 or more jokers
            grouped.containsValue(2) || grouped.containsValue(2-jokers) -> 2 // One pair. Guaranteed with 1 or more jokers
            else -> 1
        }
    }
}