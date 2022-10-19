package app;

import domain.game.Game;
import domain.game.GameObject.Colour;
import domain.game.Game.Direction;
import domain.mvc.Observer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.Border;
import persistency.XMLReader;
import persistency.XMLReader.LevelChoice;
import persistency.XMLWriter;
import rendering.Renderer;

/** 
 * The Graphical User Interface for Chap's Challenge.
 * Draws the Maze, Menu, SideBar, KeyPanel and controls the KeyMovements of the game.
 
 * 
 * @author Nina Wong [300525169]
 */

public class App extends Observer {
  
  private static final long serialVersionUID = 1L; 

  //Constants of frame
  private final int FRAME_WIDTH = 890;
  private final int FRAME_HEIGHT = 535;
 
  //Main drawing panel
  private JPanel background;
  private JDialog gameMsg; 
  private Color orange = Color.orange;

  //SideBar features
  private JLabel timerLabel;
  private JLabel levelLabel;
  private JLabel cheeseLabel;

  //KeyPane features
  private JLabel blueKey;
  private JLabel redKey;
  private JLabel greenKey;
  private JLabel totalKeys;
  private HashMap<Colour, Integer> allKeys;

  //General game components
  private Renderer renderer = null;
  private Game game = null;
  private boolean firstLoad = false;
  private boolean isResumed = false;
  private boolean won = false;
  private int lastLevel = 0;

  //Time components
  private ActionListener timerListener = null;
  private Timer timer = null;
  private int timerCount = 0;
  private int time = 0;
  private boolean isPaused = false;
  private JDialog pauseScreen = null;


  /** 
   * MVC methods.
   */

  @Override
  public void update() {
    if (game != null) {
      cheeseLabelUpdate();
      levelLabelUpdate();
      allKeys = Game.getPlayer().getAllKeys();
      blueKeyUpdate(allKeys.get(Colour.BLUE));
      redKeyUpdate(allKeys.get(Colour.RED));
      greenKeyUpdate(allKeys.get(Colour.GREEN));
      allKeysUpdate(allKeys.get(Colour.BLUE)
          +
          allKeys.get(Colour.RED)
          +
          allKeys.get(Colour.GREEN));
      renderer.draw(getGraphics());
    }
  }

  @Override
  public void updateWithMessage(String message) {
    gameMsg = new JDialog();
    gameMsg.setSize(new Dimension(300, 100));
    gameMsg.setLocationRelativeTo(this);
    JLabel msg = new JLabel(message);
    Border edge = BorderFactory.createEmptyBorder(0, 20, 0, 0);
    msg.setBorder(edge);
    msg.setAlignmentX(CENTER_ALIGNMENT);
    gameMsg.add(msg);
    gameMsg.setVisible(true);
    Timer resumeTimer = new Timer(1500, e -> { 
      gameMsg.setVisible(false);
    });
    resumeTimer.start();
  }
  
  public static void main(String[] args){
	  new App();
  }

  public App() { 
    CreateGUI(); 
    playLevel(lastLevel, false);
  }

  /**
  * Starts game and initializes a game.
  * Reads XML level file to game.

  * @author Sam Milburn and Nina Wong
  */
  public void playLevel(int level, boolean isResumed) {
    if (pauseScreen != null) {
      pauseScreen.setVisible(false);
    }
    
    isPaused = false;
    XMLReader reader = null;
    
    if (firstLoad == true) { //on first load attempt to load a saved file for specified level.
      try {
        if (level == 1) {
           reader = new XMLReader(LevelChoice.LEVEL1_save); 
        }
        else if (level == 2) {
           reader = new XMLReader(LevelChoice.LEVEL2_save); //resumes the last game if that game level was saved.
        }
        firstLoad = false; //only on first load.
      } catch (IllegalArgumentException e) {
        return;
      }
    }
    else {
      try {
        if (isResumed == true) { //if they select resume from the menu.
            if (level == 1) {
              reader = new XMLReader(LevelChoice.LEVEL1_save); 
            }
            else if (level == 2) {
              reader = new XMLReader(LevelChoice.LEVEL2_save); //resumes the last game if that game level was saved.
         }
        }
        else {
          if (level == 1) {
              reader = new XMLReader(LevelChoice.LEVEL1_default); 
          }
          else if (level == 2) {
              reader = new XMLReader(LevelChoice.LEVEL2_default); //resumes the last game if that game level was saved.
          }
        }
      } catch (IllegalArgumentException e) {
        return;
      }
    }
    
    if (reader.equals(null)) {
      reader = new XMLReader("level" + level + ".xml");
    }
    if (reader != null) {
      if (game != null) {
        game.restart(game.getLevel(), game.getPlayer());
      }
      game = new Game(reader.getLevel(), reader.getPlayer());
      attachTo(game);
      lastLevel = game.getLevel().getStage();
      for (KeyListener k : getKeyListeners()) {
        removeKeyListener(k);
      }
      addKeyListener(keyListener(game));
      Thread t1 = new Thread(game); //creates new Thread of game to handle moving enemies
      t1.start();
      renderer = new Renderer(game, game.getPlayer());
      add(renderer);
      renderer.setVisible(true);
    }
    makeLoadingScreen();
    if (timer == null) {
      setTime(game.getLevel().getTime());
      timer = new Timer(150, timerListener);
      timer.start();
    }
    else {
      timer.stop();
      setTime(game.getLevel().getTime());
      timer.restart();
    }
  }

  /**
   * Displays the rules for Chaps Challenge.
   */
  public void help() {
    String rules = "* Chap is under a time limit to collect all the treasures he needs to\n"
        +
        "  unlock the exit gate!\n" 
        +
        "* Treasures include cheese and keys, which unlock colour coded doors.\n"
        +
        "* Once Chap has collected all the treasures and unlocked all the doors, \n"
        +
        "  he is able to step on the exit tile and win! \n\n"
        +
        "* HINTS!!! \n"
        +
        "* The exit tile is black. \n"
        +
        "* The keys are red, green and blue. \n"
        +
        "* The keys corresponding doors are their same colour. \n"
        +
        "* There may be an enemy who is after you so watch out! \n";
    displayMessage(rules, "Rules");
  }

  /**
   * Displays the shortcuts for Chaps Challenge.
   */
  public void shortcuts() {
    String shorcuts = "* CTRL-X : Exit game.\n"
        +
        "* CTRL-S : Save game.\n"
        +
        "* CTRL-R : Resume saved game.\n"
        +
        "* CTRL-1 : Start a new game at level 1.\n"
        +
        "* CTRL-2 : Start a new game at level 2.\n"
        +
        "* SPACE : Pause game.\n"
        +
        "* ECS : Resume paused game.\n"
        +
        "* UP, DOWN, LEFT, RIGHT ARROWS : Move Chap within the maze.";
    displayMessage(shorcuts, "KeyBoard Shortcuts");
  }

  /**
   * Pauses the game. 
   */
  public void pause() {
    if (game == null) {
      displayAlertMessage("You have not started a game!", "Alert!");
      return;
    }
    if (isPaused()) {
      return;
    }
  
    setIsPaused();
    timer.stop();
    makePauseScreen();

    pauseScreen.setVisible(true);
    pauseScreen.addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          pauseScreen.setVisible(false);
          resume();
          }
        }
      
      @Override
      public void keyReleased(KeyEvent e) {}
      
      @Override
      public void keyTyped(KeyEvent e) {}
      });
  }

  /**
   * Creates a pause screen.  
   */
  public void makePauseScreen() {
    Image image = scaleBackground(new ImageIcon("data/icon_pause.png"), 50, 50).getImage(); 
    ImageIcon icon = new ImageIcon(image); 
    JLabel pauseIcon = new JLabel();
    pauseIcon.setIcon(icon);
    pauseIcon.setVisible(true);
    pauseScreen = new JDialog();
    pauseScreen.add(pauseIcon);
    pauseScreen.setUndecorated(true); 
    pauseScreen.setMinimumSize(new Dimension(50, 50));
    pauseScreen.pack();
    pauseScreen.setAlwaysOnTop(true);
    pauseScreen.setLocationRelativeTo(this);
  }

  /**
   * Resumes a paused game.  
   */
  public void resume() { 
    if (game == null) { 
      displayAlertMessage("You have not started a game!", "Alert!"); 
      return; 
    }
    
    if (!isPaused()) {
      return;
    }
    pauseScreen.setVisible(false);
    setIsPaused();

    Image image = scaleBackground(new ImageIcon("data/icon_play.png"), 50, 50).getImage(); 
    ImageIcon icon = new ImageIcon(image); 
    JLabel playIcon = new JLabel();
    playIcon.setIcon(icon);
    playIcon.setVisible(true);
    JDialog playScreen = new JDialog();
    playScreen.add(playIcon);
    playScreen.setUndecorated(true);  
    playScreen.setMinimumSize(new Dimension(50, 50));
    playScreen.pack(); 
    playScreen.setLocationRelativeTo(this); 
    playScreen.setAlwaysOnTop(true);
    playScreen.setVisible(true);
    Timer resumeTimer = new Timer(1000, e -> { //display resume for 1 second.
      playScreen.setVisible(false);
    });
    resumeTimer.start();
    timer = new Timer(150, timerListener);
    timer.start(); //start time from where it was prior to being paused.
  }

  /**
   * Resumes game state.
   */
  public void resumeOldGame() {
    if (pauseScreen != null) {
      pauseScreen.setVisible(false);
    }
    isPaused = false;
    Object[] buttons = {"Level 1", "Level 2"};
    int ans = JOptionPane.showOptionDialog(
        this, "What game would you like to resume?", "Resume",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
    if (ans == 0) {
      playLevel(1, true);
    }
    else if (ans == 1) {
      playLevel(2, true);
    }
  }
  
  /**
   * Saves the current game.
   */
  public void saveGame() {
    if (game.getLevel().getStage() == 1) {
      new XMLWriter(game, LevelChoice.LEVEL1_save);
    }
    else if (game.getLevel().getStage() == 2) {
      new XMLWriter(game, LevelChoice.LEVEL2_save);
    }
  }

  /**
   * Exits game.
   */
  public void exitGame() {
    timer.stop();
    for (KeyListener k : getKeyListeners()) {
      removeKeyListener(k);
    }
    if (pauseScreen != null) {
      pauseScreen.setVisible(false);
    }
    Object[] buttons = {"Yes", "No"};
    int ans = JOptionPane.showOptionDialog(
        this, "Exiting the game means your game will be lost!", "Game Exit",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
    if (ans == 0) {
      setLastLevel(game.getLevel().getStage()); //remembering last level.
      game = null;
      renderer = null;
    }
  }

  /**
   * Displays game over screen.
   */
  public void gameOver() {
    if (!gameMsg.isVisible()) {
      timer.stop();
      for (KeyListener k : getKeyListeners()) {
        removeKeyListener(k);
      }
      setTime(0);
      playLevel(game.getLevel().getStage(), false);
    }
  }
  
  /**
   * Sees if game is paused.
   * @return if game is paused of not.
   */
  public boolean isPaused() {
    if (game != null) {
      return isPaused;
    }
    return false;
  }
  
  /**
   * Sets the games pause state.
   */
  public void setIsPaused() {
    this.isPaused = !isPaused;
  }
    
  /**
   * Sets the last level to remember when starting a new game.
   */
  public void setLastLevel(int level) {
    lastLevel = level;
  }
    
  /**
   * Displays a text message on GUI.
   */
  public void displayMessage(String body, String title) {
    JOptionPane.showMessageDialog(this, body, title, JOptionPane.PLAIN_MESSAGE);
  }
    
  /**
   * Displays a text message on GUI.
   */
  public void displayAlertMessage(String body, String title) {
    JOptionPane.showMessageDialog(this, body, title, JOptionPane.ERROR_MESSAGE);
  }
    
  /**
   * @return the current game.
   */
  public Game getGame() {
    return game;
  }
    
  /**
   * @return the renderer.
   */
  public Renderer getRenderer() {
    return renderer;
  }
    
  /**
   * Creates a GUI with all of its components.
  */
  public void CreateGUI() {
    setTitle("Chap's Challenge");
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    setMaximumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBackground(Color.darkGray);
    getContentPane().setLayout(new BorderLayout());
    setResizable(false);
    background = new JPanel() {
      private static final long serialVersionUID = 1L;
      public void paintComponent(Graphics g) {
        Image image = null;
        image = scaleBackground(
            new ImageIcon("data/Background.png"), FRAME_WIDTH, FRAME_HEIGHT).getImage();
        Dimension bgSize = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
        setMinimumSize(new Dimension(bgSize));
        setMaximumSize(new Dimension(bgSize));
        setSize(new Dimension(bgSize));
        g.drawImage(image, 0, 0, null);
        }
      };
    background.setOpaque(true);
    JPanel side = addSideBar();
    side.add(keysPanel());
    background.add(side);
    add(background);
    setJMenuBar(addMenu());
    setLocationRelativeTo(null); //setting location to center screen.
    setVisible(true); //making other swing features apparent before adding the maze.
    Object[] levelChooser = {"Level 1", "Level 2"};
    int ans = JOptionPane.showOptionDialog(
        this, "Please choose a level!", "Level Selection", JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE, null, levelChooser, levelChooser[0]);
    if (ans == 0) {
      setLastLevel(1);
    }
    else {
      setLastLevel(2);
    }
    timerListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) { 
          if (game.getState().name() == "FINISHED") { 
            won = true;   
            gameOver();
            return;
            }
          timerCount += 1;
          if (time < 0) {
            gameOver();
          }
          else if (time >= 0 && !isPaused()) {
            if (timerCount == 10) {
              timerCount = 0;
              time--;
              setTime(time);
              timerLabelUpdate(time);
              }
            }
          update();
          }
        };
    pack();
  }

  /**
   * Scales the background image.
 
   * @return background image icon.
   */ 
  public ImageIcon scaleBackground(ImageIcon icon, int width, int height) {
    return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
  }
    
  /**
  * Displays the loading screen while a game is being set up.
  */
  public void makeLoadingScreen() {
    JDialog loadingScreen = new JDialog();
    Image image = scaleBackground(
        new ImageIcon("data/loading_screen.png"), FRAME_WIDTH, FRAME_HEIGHT).getImage(); 
    ImageIcon icon = new ImageIcon(image); 
    JLabel loadingIcon = new JLabel();
    loadingIcon.setIcon(icon);
    loadingIcon.setVisible(true);
    loadingScreen.add(loadingIcon); 
    loadingScreen.setUndecorated(true);  
    loadingScreen.pack(); 
    loadingScreen.setLocationRelativeTo(this); 
    loadingScreen.setVisible(true);
    Timer resumeTimer = new Timer(2000, e -> { //display resume for 3 seconds.
      loadingScreen.setVisible(false);
    });
    resumeTimer.start();
  }
    
  /**
  * Creates the JMenu Bar with its JMenu's and their JMenuItems.

  * @return a menu bar
  */
  public JMenuBar addMenu() {
    JMenuBar menubar = new JMenuBar();
    menubar.setForeground(new Color(153, 102, 0));
    menubar.add(makeGameMenu());
        
    JMenu options = null;
    options = new JMenu("Options");
    JMenuItem pause = new JMenuItem("Pause Game");
    pause.addActionListener((event) -> pause());
    pause.setForeground(new Color(153, 102, 0));
    JMenuItem resume = new JMenuItem("Resume Game");
    resume.addActionListener((event) -> resume());
    resume.setForeground(new Color(153, 102, 0));
    JMenuItem save = new JMenuItem("Save Game");
    save.addActionListener((event) -> saveGame());
    save.setForeground(new Color(153, 102, 0));
    options.add(pause);
    options.add(resume);
    options.add(save);
    menubar.add(options);
    
    JMenu levels = null;
    levels = new JMenu("Level");
    JMenuItem l1 = new JMenuItem("Level 1");
    l1.addActionListener((event) -> playLevel(1, false));
    l1.setForeground(new Color(153, 102, 0));
    JMenuItem l2 = new JMenuItem("Level 2");
    l2.addActionListener((event) -> playLevel(2, false));
    l2.setForeground(new Color(153, 102, 0));
    levels.add(l1);
    levels.add(l2);
    menubar.add(levels);
        
    JMenu help = new JMenu("Help");
    JMenuItem rules = new JMenuItem("Rules");
    rules.addActionListener((event) -> help());
    rules.setForeground(new Color(153, 102, 0));
    JMenuItem shortcuts = new JMenuItem("Shortcuts");
    shortcuts.addActionListener((event) -> shortcuts());
    shortcuts.setForeground(new Color(153, 102, 0));
    help.add(rules);
    help.add(shortcuts);
    menubar.add(help);
    menubar.setVisible(true); 

    return menubar;
  }
  
  /**
   * Creates the JMenu that handles starting and loading games.
   *
   * @return A JMenu object.
   */
  private JMenu makeGameMenu() {
	  JMenu game = new JMenu("Game");
	  JMenuItem startNew = new JMenuItem("Start New Game");
	  startNew.setForeground(new Color(153, 102, 0));
	  startNew.addActionListener((event) -> playLevel(lastLevel, false));
	  JMenuItem resumeGame = new JMenuItem("Resume Last Game");
	  resumeGame.addActionListener((event) -> resumeOldGame());
	  resumeGame.setForeground(new Color(153, 102, 0));
	  JMenuItem exit = new JMenuItem("Exit");
	  exit.addActionListener((event) -> exitGame());
	  exit.setForeground(new Color(153, 102, 0));
	  game.add(startNew);
	  game.add(resumeGame);
	  game.add(exit);
	  return game;
  }

  /**
   * Creates the components for the side bar, which update during the game.

   * @return a side bar of the changing game components - time, keys and cheese collected. 
   */
  public JPanel addSideBar() {
    JPanel sideBar = new JPanel();
    sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
    Border edge = BorderFactory.createEmptyBorder(75, 480, 105, 100);
    sideBar.setBorder(edge);

    levelLabel = new JLabel();
    levelLabel.setForeground(orange);
    levelLabel.setFont(new Font("Lucida Bright", Font.BOLD, 30));
    levelLabel.setText("   0");
    
    timerLabel = new JLabel();
    timerLabel.setForeground(orange);
    timerLabel.setFont(new Font("Lucida Bright", Font.BOLD, 30));
    timerLabel.setText("00:00");

    cheeseLabel = new JLabel();
    cheeseLabel.setForeground(orange);
    cheeseLabel.setFont(new Font("Lucida Bright", Font.BOLD, 30)); 
    cheeseLabel.setText("   0"); 

    sideBar.add(Box.createRigidArea(new Dimension(80, 10)));
    sideBar.add(levelLabel);
    sideBar.add(Box.createRigidArea(new Dimension(80, 75)));
    sideBar.add(timerLabel);
    sideBar.add(Box.createRigidArea(new Dimension(80, 65)));
    sideBar.add(cheeseLabel);
    sideBar.add(Box.createRigidArea(new Dimension(80, 45)));

    sideBar.setBackground(new Color(213, 134, 145, 123));
    sideBar.setOpaque(false);
    return sideBar;
  }

  /**
   * Updates timer label.
   */
  public void timerLabelUpdate(int time) {
    if (time < 10 && time >= 0) {
      timerLabel.setForeground(Color.red);
      timerLabel.setText("00:0" + time);
      timerLabel.setVisible(true);
    }
    else if (time == 10) {
      timerLabel.setForeground(Color.red);
      timerLabel.setText("00:" + time);
      timerLabel.setVisible(true);
    }
    else if (time > 10) {
      timerLabel.setForeground(orange);
      timerLabel.setText("00:" + time);
      timerLabel.setVisible(true);
    }
  }

  /**
   * Sets the games time.
   */
  public void setTime(int num) {
    time = num;
  }

  /**
   * Return game time.

   * @return the games time.
   */
  public int getTime() {
    return time;
  }

  /**
   * Updates key label.
   */
  public void levelLabelUpdate() {
    levelLabel.setText("   " + game.getLevel().getStage());
    levelLabel.setVisible(true);
  }

  /**
   * Updates treasure label.
   */
  public void cheeseLabelUpdate() {
    //cheeseLabel.setText("   " + game.getLevel().getTreasuresToCollect()); 
    cheeseLabel.setVisible(true);
  }

  /**
   * Creates a keys panel where all the keys collected by Chap can be viewed.

   * @return keysPanel
   */
  public JPanel keysPanel() {
    JPanel keysPanel = new JPanel();
    Border edge = BorderFactory.createEmptyBorder(0, 70, 80, 0);
    keysPanel.setLayout(new BoxLayout(keysPanel, BoxLayout.X_AXIS));
    keysPanel.setBorder(edge);

    blueKey = new JLabel();
    Image imageBlue = scaleBackground(
        new ImageIcon("data/key_blue_single.png"), 50, 50).getImage(); 
    ImageIcon iconBlue = new ImageIcon(imageBlue); 
    blueKey.setIcon(iconBlue);
    blueKey.setIconTextGap(13);
    blueKey.setForeground(orange);
    blueKey.setFont(new Font("Lucida Bright", Font.BOLD, 15));
    blueKey.setText("0");
    blueKey.setVerticalTextPosition(JLabel.BOTTOM);
    blueKey.setHorizontalTextPosition(JLabel.CENTER);

    redKey = new JLabel();
    Image imageRed = scaleBackground(new ImageIcon("data/key_red_single.png"), 50, 50).getImage(); 
    ImageIcon iconRed = new ImageIcon(imageRed); 
    redKey.setIcon(iconRed);
    redKey.setIconTextGap(13);
    redKey.setForeground(orange);
    redKey.setFont(new Font("Lucida Bright", Font.BOLD, 15));
    redKey.setText("0");
    redKey.setVerticalTextPosition(JLabel.BOTTOM);
    redKey.setHorizontalTextPosition(JLabel.CENTER);

    greenKey = new JLabel();
    Image imageGreen = scaleBackground(
        new ImageIcon("data/key_green_single.png"), 50, 50).getImage(); 
    ImageIcon iconGreen = new ImageIcon(imageGreen); 
    greenKey.setIcon(iconGreen);
    greenKey.setIconTextGap(13);
    greenKey.setForeground(orange);
    greenKey.setFont(new Font("Lucida Bright", Font.BOLD, 15));
    greenKey.setText("0");
    greenKey.setVerticalTextPosition(JLabel.BOTTOM);
    greenKey.setHorizontalTextPosition(JLabel.CENTER);

    totalKeys = new JLabel();
    totalKeys.setForeground(orange);
    totalKeys.setFont(new Font("Lucida Bright", Font.BOLD, 30));
    totalKeys.setText("0");
    Border tkBorder = BorderFactory.createEmptyBorder(0, 5, 45, 0);
    totalKeys.setBorder(tkBorder);

    keysPanel.add(blueKey);
    keysPanel.add(Box.createRigidArea(new Dimension(14, 10)));
    keysPanel.add(redKey);
    keysPanel.add(Box.createRigidArea(new Dimension(17, 10)));
    keysPanel.add(greenKey);
    keysPanel.add(Box.createRigidArea(new Dimension(23, 10)));
    keysPanel.add(totalKeys);

    keysPanel.setOpaque(false);
    keysPanel.setVisible(true);

    return keysPanel;
  }

  /**
   * Updates blueKey label.
   */
  public void blueKeyUpdate(int keys) {
    blueKey.setText(keys + "");
  }
  
  /**
   * Updates redKey label.
   */
  public void redKeyUpdate(int keys) {
    redKey.setText(keys + "");
  }
  
  /**
   * Updates greenKey label.
   */
  public void greenKeyUpdate(int keys) {
    greenKey.setText(keys + "");
  }
  
  /**
   * Updates allKeys label.
   */
  public void allKeysUpdate(int keys) {
    totalKeys.setText(keys + "");
  }

  /**
   * Controls the key movements in Chaps Challenge,
   * these could be player movements or game shortcuts.

   * @return game key listener.
   */
  public KeyListener keyListener(Game game) {
    Game currentGame = game;
    KeyListener keyListener = new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        wasPlayerMove(e.getKeyCode()); 
        wasShortcut(e.getKeyCode());
        }

      /** 
       * Checks if the keys pressed where any of the shortcuts 
       * on the menu.

       * @return if it was a GUI shortcut.
       */
      public boolean wasShortcut(int key) {
        KeyStroke save = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke resumeOld = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke level1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke level2 = KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke exit = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK);

        if (key == KeyEvent.VK_SPACE) {
          if (isPaused()) { 
            return false; 
          }
          pause();
          return true;
        }
        else if (key == KeyEvent.VK_ESCAPE) {
          resume();
          return true;
        }
        else if (key == level1.getKeyCode()) {
          if (isPaused()) { 
            return false; 
            }
          playLevel(1, false);
          return true;
        }
        else if (key == level2.getKeyCode()) {
          if (isPaused()) { 
            return false; 
            }
          playLevel(2, false);
          return true;
          }
        else if (key == save.getKeyCode()) {
          if (isPaused()) { 
            return false; 
            }
          saveGame();
          return true;
        }
        else if (key == resumeOld.getKeyCode()) {
          if (isPaused()) { 
            return false; 
          }
          resumeOldGame();
          return true;
        }
        else if (key == exit.getKeyCode()) {
          if (isPaused()) { 
            return false; 
          }
          exitGame(); 
          return true;
        } 
        return false;
      }

      /** 
       * Checks if the key pressed was a player movement.
       * Calls domain method in game class which moves player.

       * @return if it was a player move.
       */
      public boolean wasPlayerMove(int key) {
        if (isPaused()) { 
          return false; 
        }
        if (key == KeyEvent.VK_UP) {
          currentGame.movePlayer(Direction.UP);
          return true;
        }
        else if (key == KeyEvent.VK_DOWN) {
          currentGame.movePlayer(Direction.DOWN);
          return true;
        }
        else if (key == KeyEvent.VK_LEFT) {
          currentGame.movePlayer(Direction.LEFT);
          return true;
        }
        else if (key == KeyEvent.VK_RIGHT) {
          currentGame.movePlayer(Direction.RIGHT);
          return true;
        }
        return false;
        }

      /** 
       * KeyListener methods that are not used.
       */
      @Override
      public void keyReleased(KeyEvent e) {}
      
      @Override
      public void keyTyped(KeyEvent e) {}
      };
      
    return keyListener;
  }
}
