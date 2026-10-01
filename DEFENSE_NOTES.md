# Defense notes (paper-based): Bridge pattern

## One-sentence answer
Bridge separates an abstraction (`Mission`) from its implementation (`FlightPlatform`) so the two hierarchies can grow independently; they are connected by a reference field (the "bridge").

## Likely questions and short answers
1. **Which class is the Abstraction / Implementor?** Abstraction: `Mission`; Implementor: `FlightPlatform`; Refined Abstractions: `DeliveryMission`, `SurveyMission`; Concrete Implementors: `QuadcopterPlatform`, `FixedWingPlatform`.
2. **Where exactly is the bridge?** The field `platform` in `Mission` (type `FlightPlatform`).
3. **What problem does it solve?** Class explosion: 2 missions x 2 platforms would need 4 subclasses by inheritance; Bridge needs 2 + 2. With 5 x 5 it is 10 instead of 25.
4. **How do you switch implementation at runtime?** `mission.switchPlatform(other)`; the same `Mission` object then executes with different behavior (see `Main`).
5. **What do I change to add a new aircraft?** Add one class implementing `FlightPlatform`. No change in `Mission`, `DeliveryMission`, `SurveyMission` or `Main`'s logic.
6. **What do I change to add a new mission?** Add one subclass of `Mission` that calls `platform()` methods. No platform is touched.
7. **Bridge vs Adapter?** Adapter fixes an incompatibility between existing interfaces after the fact; Bridge is designed up front to let abstraction and implementation vary separately.
8. **Bridge vs Strategy?** Structurally similar (composition + interface). Strategy swaps one algorithm of one class; Bridge splits a whole class hierarchy in two and the abstraction usually has several refined variants.
9. **Why an abstract class for the Abstraction and an interface for the Implementor?** The abstraction holds state (the reference) and shared code (`switchPlatform`, `describe`); the implementor is a pure contract, so any class can implement it.
10. **Why is `execute()` not duplicated per platform?** The order of steps is defined once in the mission; platforms only implement single actions.
11. **Which Clean Code principles did you use?** See README (separation of responsibilities, naming, small classes, no duplication, open/closed, composition, fail fast, no magic numbers).
12. **Why `FlightLog`?** To avoid duplicated formatting code in the two concrete platforms.
13. **Why a `RecordingPlatform` in tests?** It is a third Implementor; because missions depend only on the interface, they can be tested without any real aircraft logic.

## Paper trace to practise
`delivery.execute()` with `QuadcopterPlatform`: `takeOff(60)` -> `flyTo(400,250)` -> `holdPosition(3)` -> `releasePayload()` -> `flyTo(HOME)` -> `land()`. After `switchPlatform(fixedWing)` the same six calls happen, but the printed behavior is different (loiter instead of hover).
