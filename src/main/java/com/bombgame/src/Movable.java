package com.bombgame.src;

import com.bombgame.src.enums.Direction;

public interface Movable extends Placeable {
    void move(Direction direction);
}
