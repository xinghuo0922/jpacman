package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class PlayerCollisionsTest {
    public PlayerCollisions  playerCollisions;
    public PointCalculator pointCalculator;

    @BeforeEach
    void before(){
        pointCalculator = mock(PointCalculator.class);
        playerCollisions = new PlayerCollisions(pointCalculator);
    }

    @Test
    @DisplayName("玩家撞向幽灵")
    public void test1(){
        Player player = mock(Player.class);
        Ghost ghost = mock(Ghost.class);
        playerCollisions.collide(player, ghost); //碰撞体为player，被碰撞体是ghost

        verify(pointCalculator, times(1)).collidedWithAGhost(player, ghost);
        verify(player, times(1)).setAlive(false); //按照规则player被消灭
        verify(player, times(1)).setKiller(ghost);  //被ghost消灭
    }

    @Test
    @DisplayName("幽灵撞向玩家")
    public void test2() {
        Player player = mock(Player.class);
        Ghost ghost = mock(Ghost.class);
        playerCollisions.collide(ghost, player); //碰撞体为ghost，被碰撞体是player

        verify(pointCalculator, times(1)).collidedWithAGhost(player, ghost);
        verify(player, times(1)).setAlive(false); //按照规则player被消灭
        verify(player, times(1)).setKiller(ghost);  //被ghost消灭
    }

//     //项目貌似没有考虑幽灵与幽灵碰撞
//    @Test
//    @DisplayName("幽灵撞向幽灵")
//    public void test3() {
//        Ghost ghost = mock(Ghost.class);
//        playerCollisions.collide(ghost, ghost); //碰撞体为ghost，被碰撞体是ghost
//
//        verify(pointCalculator, times(1)).collidedWithAGhost(ghost, ghost);
//    }

    @Test
    @DisplayName("玩家往豆子处移动")
    public void test3() {
        Player player = mock(Player.class);
        Pellet pellet = mock(Pellet.class);
        playerCollisions.collide(pellet, player); //碰撞体为player，被碰撞体是豆子

        verify(pointCalculator, times(1)).consumedAPellet(player, pellet);
        verify(pellet, times(1)).leaveSquare(); //按规则豆子被吃掉
    }

}
