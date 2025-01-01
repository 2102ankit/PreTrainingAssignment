package oopswithexceptions.polymorphism;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Enum for piece color
enum Color {
    WHITE, BLACK;
}

// Enum for chess piece names
enum PieceName {
    PAWN, KNIGHT, ROOK, BISHOP, QUEEN, KING;
}

class Position {
    char file;
    int rank;

    Position(char file, int rank) {
        if (file < 'a' || file > 'h')
            throw new IllegalArgumentException("Invalid file : " + file + " , file must be between 'a' & 'h'");

        if (rank < 1 || rank > 8)
            throw new IllegalArgumentException("Invalid rank : " + rank + " , rank must be between 1 & 8");

        this.file = file;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "" + file + rank;
    }
}


abstract class ChessPiece {
    static Logger logger = LogManager.getLogger();

    // Color of the piece (black or white)
    private Color color;

    // Name of the piece (Pawn, Knight, etc.)
    private PieceName name;

    // Starting position of piece ('e2','a4', etc.)
    private Position position;

    //constructor
    public ChessPiece(Color color, PieceName name, Position position) {
        this.color = color;
        this.name = name;
        this.position = position;
    }

    // Getters for color and name
    public Color getColor() {
        return color;
    }

    public String getName() {
        return name.toString().toLowerCase();
    }


    //method for moving a piece
    void performMove(Position initialPosition, Position finalPosition) {
        logger.debug("Piece moved from {} to {}", initialPosition, finalPosition);
        this.position = finalPosition;
    }

    //overloaded method for capturing while moving a piece
    void performMove(Position initialPosition, Position finalPosition, ChessPiece captured) {
        logger.debug("Piece moved from {} to {} takes piece {}", initialPosition, finalPosition, captured);
        this.position = finalPosition;
    }

    // Abstract method to define piece-specific behavior for moving
    abstract void move(Position from, Position to);
}

//Concrete class for Pawn
class Pawn extends ChessPiece {
    public Pawn(Color color, Position position){
        super(color,PieceName.PAWN,position);
    }

    @Override
    void move(Position from, Position to) {
        logger.debug("Pawn moves from {} to {}", from, to);
    }
}

// Concrete class for a Knight
class Knight extends ChessPiece {
    public Knight(Color color, Position position){
        super(color, PieceName.KNIGHT, position);
    }

    @Override
    void move(Position from, Position to) {
        logger.debug("Knight moves from {} to {}", from, to);
    }
}

// Concrete class for a Rook
class Rook extends ChessPiece {
    public Rook(Color color, Position position){
        super(color, PieceName.ROOK, position);
    }

    @Override
    void move(Position from, Position to) {
        logger.debug("Rook moves from {} to {}", from, to);
    }
}

// Concrete class for a Bishop
class Bishop extends ChessPiece {
    public Bishop(Color color, Position position){
        super(color, PieceName.BISHOP, position);
    }

    @Override
    void move(Position from, Position to) {
        logger.debug("Bishop moves from {} to {}", from, to);
    }
}

// Concrete class for a Queen
class Queen extends ChessPiece {
    public Queen(Color color, Position position){
        super(color, PieceName.QUEEN, position);
    }

    @Override
    void move(Position from, Position to) {
        logger.debug("Queen moves from {} to {}", from, to);
    }
}

// Concrete class for a King
class King extends ChessPiece {
    public King(Color color, Position position){
        super(color, PieceName.KING, position);
    }

    @Override
    void move(Position from, Position to) {
        logger.debug("King moves from {} to {}", from, to);
    }
}

public class ChessGame {
    static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Position from, to;

        try {
            from = new Position('e', 2);
            to = new Position('f', 4);
        } catch (IllegalArgumentException e) {
            logger.error("Error while creating position: " + e.getMessage());
            return;
        }

        ChessPiece[] pieces;

        try {
            // Create an array of chess pieces with the corresponding positions and colors
            pieces = new ChessPiece[] {
                    new Pawn(Color.WHITE, new Position('d', 2)),    // White Pawn at d2
                    new Knight(Color.WHITE, new Position('f', 3)),  // White Knight at f3
                    new Rook(Color.BLACK, new Position('h', 8)),    // Black Rook at h8
                    new Bishop(Color.WHITE, new Position('c', 3)),  // White Bishop at c3
                    new Queen(Color.BLACK, new Position('e', 8)),   // Black Queen at e8
                    new King(Color.WHITE, new Position('e', 1)),     // White King at e1
                    new Knight(Color.WHITE, new Position('e', 9)),  //Wrong initialization
            };
        }catch (IllegalArgumentException e){
            logger.error("Error while creating position: " + e.getMessage());
            return;
        }
        //move pieces for each piece in ChessPiece Array
        for(ChessPiece piece : pieces){
            try {
                piece.performMove(from,to);
                piece.move(from,to);
            }catch (IllegalArgumentException e){
                logger.error("Error while creating {}: " + e.getMessage(), piece);
                return;
            }
        }

    }
}
