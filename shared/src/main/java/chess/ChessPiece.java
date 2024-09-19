package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece piece = board.getPiece(myPosition);
//        Rule rule = switch (getPieceType()) {
//            case BISHOP -> new Rule(true, new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1}});
//            case ROOK   -> new Rule(true, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
//            case KNIGHT -> new Rule(false, new int[][]{{2, 1}, {2, -1}, {-2, 1}});
//            case QUEEN  -> new Rule(true, new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1}});
//            case KING   -> new Rule(false, new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1}});
//            default -> null;
//        };
//
//        return rule.getMoves(board, pos);


        return null;

    }

    public  String toString() {
        return String.format("(%s, %s)", this.pieceType.toString(), this.pieceColor.toString());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return this.pieceType.equals(that.pieceType);
    }

    public int hashCode() {
        return this.pieceType.hashCode();
    }

}
