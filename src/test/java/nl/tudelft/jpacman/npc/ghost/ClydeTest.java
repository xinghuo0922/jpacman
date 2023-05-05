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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClydeTest {
    static GhostMapParser ghostMapParser;
    //初始化地图解析器
    @BeforeAll
    @DisplayName("创建mapParser")
    static void createInstances(){
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

    @DisplayName("距离小于八个单位")
    @Test
    @Order(1)
    void test1(){
        List<String> mapList = new ArrayList<>();
        mapList.add("##############");
        mapList.add("#.#...P.....C#");
        mapList.add("##############");
        Level level = ghostMapParser.parseMap(mapList);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        //创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);

        Optional<Direction> optionalDirection = clyde.nextAiMove();
        //验证行为方向
        assertThat(optionalDirection.get()).isEqualTo(Direction.EAST);
    }

    @DisplayName("距离大于八个单位")
    @Test
    @Order(2)
    void test2(){
        List<String> mapList = new ArrayList<>();
        mapList.add("##############");
        mapList.add("C.........P###");
        mapList.add("##############");
        Level level = ghostMapParser.parseMap(mapList);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        //创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);

        Optional<Direction> optionalDirection = clyde.nextAiMove();
        //验证行为方向
        assertThat(optionalDirection.get()).isEqualTo(Direction.EAST);
    }

    @DisplayName("地图中不存在player对象")
    @Test
    @Order(3)
    void test3(){
        List<String> mapList = new ArrayList<>();
        mapList.add("####..##..####");
        mapList.add(".###.C#..###.#");
        mapList.add("###..##..#####");
        Level level = ghostMapParser.parseMap(mapList);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(level.isAnyPlayerAlive()).isFalse(); //此时没有player对象
        Optional<Direction> optionalDirection = clyde.nextAiMove();
        //因为没有player对象，此时不会移动
        assertThat(optionalDirection.isPresent()).isFalse();
    }

    @DisplayName("player与clyde没有可达路径")
    @Test
    @Order(4)
    void test4(){
        List<String> mapList = new ArrayList<>();
        mapList.add("##############");
        mapList.add(".###.P...##..C");
        mapList.add("##############");
        Level level = ghostMapParser.parseMap(mapList);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        //创建player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);

        Optional<Direction> optionalDirection = clyde.nextAiMove();
        //没有路径，返回为空
        assertThat(optionalDirection.empty());
    }
}
