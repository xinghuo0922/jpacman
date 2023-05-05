package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.*;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Inky 考虑两件事：Blinky 的位置，以及吃豆人前面两个网格空间的位置。
 * Inky 从 Blinky 到吃豆人前面两个方格的位置画了一条线，并将这条线延长两倍。
 * 因此，如果 Inky 在吃豆人后面时与 Blinky 并肩作战，那么 Inky 通常会一直跟随 Blinky。
 * 但是，如果 Inky 在吃豆人前面，而 Blinky 远远落后于他，那么 Inky 往往会想要远离吃豆人（实际上，远远领先于吃豆人）。
 * Inky 受到影响 Speedy 的类似目标错误的影响。当吃豆人移动或面朝上时，Inky 用来画线的位置是吃豆人上方和左侧两个方格。
 **/

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InkyTest {
    static GhostMapParser ghostMapParser;
    //初始化地图解析器
    @BeforeAll
    @DisplayName("创建mapParser")
    static void createInstances() {
        PacManSprites sprites = new PacManSprites();
        GhostFactory ghostFactory = new GhostFactory(sprites);
        PointCalculator pointCalculator = new PointCalculator() {
            @Override
            public void collidedWithAGhost(Player player, Ghost ghost) {}
            @Override
            public void consumedAPellet(Player player, Pellet pellet) {}
            @Override
            public void pacmanMoved(Player player, Direction direction) {}
        };
        LevelFactory levelFactory = new LevelFactory(sprites,ghostFactory,pointCalculator);
        BoardFactory boardFactory = new BoardFactory(sprites);
        ghostMapParser = new GhostMapParser(levelFactory,boardFactory,ghostFactory);
    }

    @DisplayName("没有Blinky对象")
    @Test
    @Order(1)
    void test1(){
        List<String> mapList = new ArrayList<>();
        mapList.add("##############");
        mapList.add("#####I......P#");
        mapList.add("##############");
        Level level = ghostMapParser.parseMap(mapList);
        //找到inky对象、blinky对象
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        //初始化player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);
        //此时找不到blinky对象
        assertThat(blinky).isNull();

        Optional<Direction> optionalDirection = inky.nextAiMove();
        //找不到blinky对象，此时不会移动
        assertThat(optionalDirection.isPresent()).isFalse();
    }

    @DisplayName("没有Player对象")
    @Test
    @Order(2)
    void test2(){
        List<String> mapList = new ArrayList<>();
        mapList.add("##############");
        mapList.add("#####I..B...##");
        mapList.add("##############");
        Level level = ghostMapParser.parseMap(mapList);
        //找到inky对象、blinky对象
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        //此时找不到Player对象
        assertThat(level.isAnyPlayerAlive()).isFalse();

        Optional<Direction> optionalDirection = inky.nextAiMove();
        //找不到blinky对象，此时不会移动
        assertThat(optionalDirection.isPresent()).isFalse();
    }

    @DisplayName("inky无到达路径")
    @Test
    @Order(3)
    void test3(){
        List<String> mapList = new ArrayList<>();
        mapList.add("##############");
        mapList.add("#I.##B......P#");
        mapList.add("##############");
        Level level = ghostMapParser.parseMap(mapList);
        //找到inky对象、blinky对象
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        //初始化player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);

        Optional<Direction> optionalDirection = inky.nextAiMove();
        //没有路径，返回为空
        assertThat(optionalDirection.empty());
    }
    @DisplayName("Blinky无到达路径")
    @Test
    @Order(4)
    void test4(){
        List<String> mapList = new ArrayList<>();
        mapList.add("##############");
        mapList.add("#I..B....#..P#");
        mapList.add("##############");
        Level level = ghostMapParser.parseMap(mapList);
        //找到inky对象、blinky对象
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        //初始化player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);

        Optional<Direction> optionalDirection = inky.nextAiMove();
        //没有路径，返回为空
        assertThat(optionalDirection.empty());
    }

    @DisplayName("正常情况")
    @Test
    @Order(5)
    void test5(){
        List<String> mapList = new ArrayList<>();
        mapList.add("##############");
        mapList.add("#I...B...#####");
        mapList.add("########.#####");
        mapList.add("########P#####");
        Level level = ghostMapParser.parseMap(mapList);
        //找到inky对象、blinky对象
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());
        //初始化player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);

        Optional<Direction> optionalDirection = inky.nextAiMove();
        //验证行为方向
        assertThat(optionalDirection.get()).isEqualTo(Direction.EAST);
    }
}
