## Project Overview
- JavaFX 21 modular app (module de.esnecca) seeded by App in [src/main/java/de/esnecca/App.java](src/main/java/de/esnecca/App.java); UI is a BorderPane with a scrollable canvas and Size/Start/Stop controls.
- World state lives in XMachine ([src/main/java/de/esnecca/XMachine.java](src/main/java/de/esnecca/XMachine.java)); it wires the canvas, creates initial grass/sheep, and rotates todo/done queues each tick.
- Rendering is pixel-level via XCanvas ([src/main/java/de/esnecca/XCanvas.java](src/main/java/de/esnecca/XCanvas.java)); all entities must update RGB buffers with set(...) then trigger paint() indirectly through XMachine.iterate().

## Simulation Architecture
- XField ([src/main/java/de/esnecca/XField.java](src/main/java/de/esnecca/XField.java)) stores a toroidal grid of XObject cells; every cell has an intrinsic lock from XObject ([src/main/java/de/esnecca/XObject.java](src/main/java/de/esnecca/XObject.java)).
- Worker threads (32 instances of XThread in [src/main/java/de/esnecca/XThread.java](src/main/java/de/esnecca/XThread.java)) call XMachine.step(); XMainThread ([src/main/java/de/esnecca/XMainThread.java](src/main/java/de/esnecca/XMainThread.java)) owns the iterate loop and swaps todo/done queues.
- Queue discipline: entities ready to process live in todo; after step() they must be requeued via done.add(...) or setAndDone(...). Bypass these helpers only if you also maintain locking and queue invariants.
- Movement-capable actors extend XLock ([src/main/java/de/esnecca/XLock.java](src/main/java/de/esnecca/XLock.java)), which reserves the 8 neighboring cells by locking in a directionally-biased order and wraps coordinates.
- Always free() any array returned from reserve(); leaving a lock held will deadlock worker threads and stall iterate().

## Entity Behavior Patterns
- Passive terrain: XGrass ages and paints green intensity proportional to age ([src/main/java/de/esnecca/XGrass.java](src/main/java/de/esnecca/XGrass.java)). Replace grass by calling XMachine.setAndDone(...), not by writing the grid directly.
- Herbivores: XSheep consumes grass with stochastic search order, tracks food as both red and green channel, and reproduces when food overflows ([src/main/java/de/esnecca/XSheep.java](src/main/java/de/esnecca/XSheep.java)). Respect the food bookkeeping to keep reproduction tuned.
- Predators: XWolf, XWolf2, and XWolf3 differ mainly in hunger drain, movement bias, and reproduction color channel ([src/main/java/de/esnecca/XWolf.java](src/main/java/de/esnecca/XWolf.java), [src/main/java/de/esnecca/XWolf2.java](src/main/java/de/esnecca/XWolf2.java), [src/main/java/de/esnecca/XWolf3.java](src/main/java/de/esnecca/XWolf3.java)). Use the existing hunger thresholds and grass replacement rules when adding variants.
- Mouse input in XMachine.handle() replaces the clicked cell with wolves (primary/secondary buttons) or a sheep (other buttons); new species must be plumbed through the same setNew()/setAndDone() path.

## Concurrency and Rendering Notes
- XObject.iterate(...) should return true once state and canvas are updated; return false only when deferring work so the object requeues untouched.
- Any method that changes a cell should call getLock() before mutation, release it, and ensure the object re-enters done/todo so workers keep cycling entities.
- Color channels map directly to bytes in the backing array; keep values within 0-255 to avoid overflow artifacts in XCanvas.paint().
- Avoid costly per-pixel loops during iterate(); rely on queue batching (XQueue.getMany()) to amortize work across objects.

## Build & Run
- Project uses Maven with Java 21; run `mvn clean javafx:run` (or use run-java21.ps1) to launch with javafx-maven-plugin targeting main class de.esnecca.App.
- For local builds/tests on Windows, use build-java21.ps1 to pin JAVA_HOME to C:\Install\jdk-21.0.2 before running mvn clean compile/test.
- If JAVA_HOME differs, adjust the PowerShell scripts or set the environment variable before invoking Maven.

## Extending the Simulation
- Introduce new actors by subclassing XLock, override iterate() to consume reserve()/free(), and schedule reproduction or death via XMachine helpers.
- When adding UI controls, ensure long-running work stays on XMachine/XThread; keep JavaFX Application Thread limited to event handlers and canvas swaps.
- Document any new queue or locking rules here so future agents maintain consistency.
