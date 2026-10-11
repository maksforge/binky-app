package app.binky.tracker.tip

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class TipAskTest {
    private val installedAt = Instant.parse("2026-09-01T08:00:00Z")
    private val monthLater = installedAt + TIP_ASK_AFTER

    private fun due(
        asked: Boolean = false,
        records: Int = TIP_ASK_AFTER_RECORDS,
        unwell: Boolean = false,
        now: Instant = monthLater,
    ) = tipAskDue(asked, records, unwell, installedAt, now)

    @Test
    fun `due once installed a month with enough recorded`() {
        assertTrue(due())
    }

    @Test
    fun `not due a millisecond short of the month`() {
        assertFalse(due(records = 1_000, now = monthLater.minusMillis(1)))
    }

    @Test
    fun `not due one record short`() {
        assertFalse(due(records = TIP_ASK_AFTER_RECORDS - 1, now = monthLater.plus(TIP_ASK_AFTER)))
    }

    @Test
    fun `never while a bunny is unwell`() {
        assertFalse(due(unwell = true, records = 1_000))
    }

    @Test
    fun `never again once answered`() {
        assertFalse(due(asked = true, records = 1_000, now = monthLater.plus(TIP_ASK_AFTER)))
    }
}
