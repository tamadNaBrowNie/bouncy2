# Particle Physics Simulator on Java

DEMO VIDEO: [https://drive.google.com/file/d/1yxdbIFTXnD6hJwBhKlFrFCfu71CpvGeO/view?usp=sharing](https://drive.google.com/file/d/1g1bjDGhr6YqjmY64ekCRlfWT6URu7z70/view?usp=sharing)

### HOW TO USE
- Particles can be spawned in three (3) modes, represented by the tabs on the top left.
  - Add by Distance _(default)_
  - Add by Velocity
  - Add by Angle
- In order to spawn particles, numerical inputs must be placed on the appropriate fields. Blank fields will be considered Invalid Input and particles will not be spawned.
- The tab "Add by Distance" will show only the fields for Start/End Points, Constant Velocity, and Constant Angle.
- Similarly, under the tab "Add by Angle", only the fields for Starting Points X/Y, and Constant Velocity will be shown.
- And on the "Add by Velocity" tab, only the fields for Starting Points X/Y, and Constant Angle will be shown.
- Among all three modes, the inputs that are similar in nature (ex. taking constant velocity, taking starting X and Y coordinates) are shared, as well as the number of particles to spawn.
- The particles will spawn as red Circles in the pane on the right with an area of 1280,720.
- There is an FPS counter on the top right of the right pane indicating the program's frame rate.
- On the Left side, there are 2 new input boxes and a button. Put the x and y coordinates respectively of where you want to spawn as an explorer and press the button or 'E' to enter exploration.
- Move with 'WASD'. Press 'E' or the button to go back to Debug mode. While exploring, adding particles is disabled.
- Jars are in the DEMO folder. They require Java 8 or at least JavaFX 8 to run.
