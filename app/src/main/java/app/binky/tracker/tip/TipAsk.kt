package app.binky.tracker.tip

import java.time.Duration
import java.time.Instant

/** Weighings plus observations, across every bunny, before the tip is mentioned. */
const val TIP_ASK_AFTER_RECORDS = 10

/** How long Binky has to have been installed before the tip is mentioned. */
val TIP_ASK_AFTER: Duration = Duration.ofDays(30)

/**
 * Whether Home should mention the tip — **once per install, to an owner who already uses the app,
 * and never while a bunny is unwell** (ADR-0011's one ask, revived by its Play Billing amendment).
 *
 * [installedAt] is the package's `firstInstallTime`, which a restore onto a new phone does not carry,
 * so a moved install waits its own month. [asked] lives in preferences, which survive ADR-0007's
 * wipes and travel in every export, so an owner who has answered is never asked on their next phone.
 *
 * [anyBunnyUnwell] is a running watch or a medication course on an active bunny: someone who opened
 * the app because their bunny is ill should not be asked for money.
 */
fun tipAskDue(
    asked: Boolean,
    records: Int,
    anyBunnyUnwell: Boolean,
    installedAt: Instant,
    now: Instant,
): Boolean =
    !asked &&
        !anyBunnyUnwell &&
        records >= TIP_ASK_AFTER_RECORDS &&
        Duration.between(installedAt, now) >= TIP_ASK_AFTER
