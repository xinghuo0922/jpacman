package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.*;
import org.mockito.internal.matchers.Null;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MapParserTest {
    private MapParser mapParser;
    private final LevelFactory levelFactory = mock(LevelFactory.class);//Mock方法内部new出来的对象
    private final BoardFactory boardFactory = mock(BoardFactory.class);

    @BeforeEach
    void setup() {
        mapParser = new MapParser(levelFactory, boardFactory);

        when(boardFactory.createGround()).thenReturn(mock(Square.class));//给测试类中的静态类调静态方法提供返回值
        when(boardFactory.createWall()).thenReturn(mock(Square.class));

        when(levelFactory.createGhost()).thenReturn(mock(Ghost.class));
        when(levelFactory.createPellet()).thenReturn(mock(Pellet.class));
    }

    @Test
    @Order(1)
    @DisplayName("读取不存在的文件")
    void test1() {
        String file = "/notexistmap.txt";
        assertThatThrownBy(() -> {
            mapParser.parseMap(file);
        }).isInstanceOf(PacmanConfigurationException.class).hasMessage("Could not get resource for: " + file);
    }

    @Test
    @Order(2)
    @DisplayName("存在的文件")
    void test2() throws IOException {
        String file = "/simplemap.txt";
        mapParser.parseMap(file);

        verify(boardFactory, times(4)).createGround();
        verify(boardFactory, times(2)).createWall();
        verify(levelFactory, times(1)).createGhost();
    }

    @Test
    @Order(3)
    @DisplayName("不能识别的地图文件")
    void test3() {
        String file = "/unrecognizedcharmap.txt";
        assertThatThrownBy(() -> {
            mapParser.parseMap(file);
        }).isInstanceOf(PacmanConfigurationException.class);
    }

    @Test
    @Order(4)
    @DisplayName("地图文件输入文本为空")
    void test4() {
        List<String> file = null;
        assertThatThrownBy(() -> {
            mapParser.parseMap(file);
        }).isInstanceOf(PacmanConfigurationException.class).hasMessage("Input text cannot be null.");
    }

    @Test
    @Order(5)
    @DisplayName("地图文件输入文本必须至少包含一行")
    void test5() {
        String file = "/emptymap.txt";
        assertThatThrownBy(() -> {
            mapParser.parseMap(file);
        }).isInstanceOf(PacmanConfigurationException.class).hasMessage("Input text must consist of at least 1 row.");
    }


}
