Lightning Optimizer

A lightweight, open-source performance utility for Minecraft that reduces lag spikes and improves frame stability.

What is Lightning Optimizer?
Lightning Optimizer is a surgical, minimalistic performance mod focused on one thing: making your game run smoother. Instead of rewriting the entire rendering engine, it applies targeted micro-optimizations to reduce CPU overhead, stabilize frame times, and eliminate stutters — with zero configuration required.
Just drop it in your mods folder and feel the difference.

Features

Engine Micro-Optimizations — Reduces unnecessary CPU overhead for higher average FPS
Stutter & Lag Spike Reduction — Stabilizes frame times during chunk loading and heavy gameplay
Particle Culling — Intelligently skips rendering of distant and invisible particles
Real-Time HUD — Displays live FPS and frame time overlay (toggle with F8)
Zero Configuration — Works out of the box, no setup needed
Modpack Friendly — Designed to run alongside other optimization mods without conflicts


Building from Source
Requirements:

JDK 21 or newer
Internet connection (dependencies are downloaded automatically on first build)

Steps:
bashgit clone https://github.com/hydrogencode/lightningoptimizer.git
cd lightningoptimizer
./gradlew build
The compiled .jar will be located in build/libs/.

Modpack Policy
You are free to include Lightning Optimizer in any public or private modpack without asking for permission. A link back to the CurseForge page is appreciated but not required.

Contributing
Pull requests and issue reports are welcome. If you find a bug or have a suggestion, please open an issue.

License
This project is licensed under the MIT License.

<p align="center">Made by <a href="https://www.curseforge.com/members/hydrogen001">hydrogen001</a></p>
