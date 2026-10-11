# Donations are one-off, external, and unlock nothing

The app is free and ad-free with no running costs, so there is nothing to fund — support is a thank-you,
not a business model. Supporters are directed to a Buy Me a Coffee page for a **one-off** payment. Patreon
was rejected: recurring membership suits ongoing creative output, not a quiet utility someone uses for
years, and it commits the author to maintaining a patron relationship.

**A donation must never unlock anything.** No features behind it, no supporter tier, no reminder that you
have not paid. This keeps the app honestly free and keeps it clear of digital-goods payment policy
entirely.

The supporter credits list is **baked into each release**, not fetched. The app's **own code** makes no
network requests at all — no analytics, no telemetry, no fetched content — which is a real property worth
protecting for something holding a pet's medical records; trading it away so a thank-you list updates
sooner would be a bad deal. Names are added by hand from the Buy Me a Coffee supporters export at release
time, and only where the supporter opted into being named; everyone else is a count of anonymous
supporters.

The one qualification is not our code: the **optional** ML Kit document scanner is delivered by Google
Play services, which may download its module over the network on first use and processes the scan in
Google's process (ADR-0009). So the honest claim is "the app's own code makes no network requests", not
"the app makes none at all". An owner who wants zero Google involvement can decline scanning and use the
plain-camera fallback, which ADR-0009 already provides.

## The one ask

The app interrupts once, and only for someone already familiar with it: after roughly 30 days *and* a
meaningful amount of data entered. Elapsed time alone would fire at someone who installed it, added
nothing and forgot.

It is shown **once**. Dismissing is permanent — no "remind me later", no second attempt, and it does not
return after an app update. It is never shown while a bunny is under a Watch or on an active medication
course: someone who opened the app because their bunny is ill should not be asked for money.

A permanent "Support this app" entry in settings covers everyone who dismissed it or grew grateful later.
The wording is thanks, not need — there are no bills to plead, and saying otherwise would be a lie.

## Consequences

The dismissal flag lives in preferences, **not in the database** — ADR-0007 allows the database to be
wiped by a schema change, which must never resurrect the prompt.


Google Play's rules on linking to external payment have shifted repeatedly. Re-read the current policy
before publishing rather than assuming this arrangement is still permitted; an in-app "tip" purchase
through Play Billing is the fallback if it is not.

## Amendment (Phase 6): the re-read happened, and the arrangement is withdrawn

**The paragraph immediately above is the instruction that closed this ADR.** The policy was re-read while
planning the Support screen, and the arrangement it describes is not permitted. Nothing here was ever
built — no prompt, no settings entry, no supporter list, no link — so this is a plan withdrawn rather than
code removed, and the app has never shown a donation ask of any kind.

Play's **Payments** policy §3 exempts only **tax-exempt** donations from Play Billing, and §4 forbids an
app leading users to any other payment method "including through in-app user interface flows". A Buy Me a
Coffee page paying an individual is income, not a tax-exempt donation, so neither half of the policy helps
— the exemption does not apply and the prohibition does. This is not a cautious reading:
**StreetComplete** — free, open source, no ads — was **rejected** under this policy for in-app Patreon,
Liberapay and GitHub Sponsors links, and the flagged item was a link to a project page that merely
*contained* donation information. The 2026 external-payment-links program does not rescue it either: it is
region-limited and scoped to purchases of in-app digital items, not donations.

The fallback this ADR named is real but costs more than it buys. A Play Billing tip jar would flip the
Console's in-app-purchase declaration, put an **In-app purchases** badge on a listing whose whole pitch is
that it is free, contradict two answers already recorded in [`play-app-content.md`](../play-app-content.md),
and add a billing library to a manifest that two release audits were spent keeping clean.

So the specifics above are dead: the Buy Me a Coffee link, the one 30-day ask, the permanent *Support this
app* entry in settings, the baked-in supporter credits, and the dismissal flag in preferences that existed
to serve them. **What replaces the ask is a rating** — Phase 6's Support screen links to the Play listing
instead, which costs nothing on the Console, adds no dependency, and is worth more to a listing with no
reviews than a tip would be. See [`phase-6.md`](../phase-6.md) for the full argument and for why it is a
plain store link rather than the In-App Review API.

Two things in this ADR survive it, both stated more strongly than before:

- **The app is free with nothing behind a payment**, which is now unconditional rather than a rule about
  what a donation may not unlock. There is no payment surface anywhere in the app to attach a condition to.
- **The app's own code makes no network requests.** That was the reason the supporter list was to be baked
  into each release; the list is gone, but the property it was protecting is untouched and is what the
  privacy policy and the Data safety answer both rest on. Its one qualification is still ML Kit's, exactly
  as described above.

## Amendment (1.10): the tip comes back, through Play Billing

**The owner has decided on the fallback this ADR named**, and it is a different question from the one the
Phase 6 amendment answered. That amendment was about **leading users to an outside payment page**, which
Payments §4 forbids; it stands, and there is still no external link anywhere in the app. A one-time
in-app product sold through **Google Play Billing** is Play's own payment system and needs no exemption at
all. The Phase 6 amendment did not find it disallowed — it found it not worth its costs. Those costs are
now accepted, each with what it actually came to:

- **The *In-app purchases* badge** appears on the listing once the products exist. The listing's "no in-app
  purchases" claim is removed in all nine locales; "free, with no paid tier and nothing locked" stays true.
- **The two answers in [`play-app-content.md`](../play-app-content.md)** — IARC's "purchase digital goods"
  and the §7 note that no billing ships — are re-answered there, with the Data safety judgement written down.
- **The billing library in the manifest** adds exactly one permission to the artifact,
  `com.android.vending.BILLING`, now expected by `scripts/aab-permissions.py`. Its telemetry dependency
  merges `INTERNET` and `ACCESS_NETWORK_STATE`, and both were already there — ML Kit's and WorkManager's —
  so nothing is removed and nothing new is claimed. A purchase is an IPC call to the Play Store app,
  which does the networking in its own process: **the app's own code still makes no network requests.**

### What ships

- **Four consumable one-time products**, `tip_small`, `tip_medium`, `tip_large` and `tip_xlarge`, at 5,
  10, 20 and 50 PLN, each with Play's multi-quantity on; Play converts each for other countries. The
  buttons wrap, so four prices fit a narrow phone. The app never states a price of its own — it shows Play's
  `formattedPrice` — and shows no tip at all where Play returns no products: no Play, the debug
  `applicationId`, or products not yet created in the Console.
- **Every purchase is consumed, on every foreground.** Consuming is what lets the same tip be given twice,
  and it also satisfies Play's three-day acknowledgement rule — Play refunds anything not acknowledged by
  then. So owned purchases are settled whenever the app comes to the foreground, not only when Support
  is open: a payment that clears while the app is closed, or one caught by a process death, is consumed
  the next time the owner opens it. One consume covers a purchase's whole quantity. There is no
  entitlement to keep, so nothing about a purchase is ever stored.
- **The permanent entry is a card on Support**, not in Settings: Support is the app's only About-shaped
  screen, and it already carries the rating.
- **The one ask is a card on Home**, revived from this ADR's original section with its rules intact:
  once per install, after **30 days installed** (`firstInstallTime`) **and ten weighings plus
  observations** across every bunny, never while an active bunny is under a watch or on a medication
  course, never in an archived scope, and only where Play will sell a tip. *Leave a tip* and *No thanks*
  both end it. The answered flag lives in preferences, not the database, for the reason given under
  *Consequences* above.
- **The wording is "tip the developer"**, never "donate" or "support the project" — it is a payment to a
  person and buys nothing, and CONTEXT.md now carries **Tip** as a term.

### What stays dead

**The supporter credits list.** Nobody opts into being named through a Play purchase, and fetching a
list would break the property this ADR was most careful to protect. And **nothing a tip could unlock will ever
exist**: every feature ships to everyone.
