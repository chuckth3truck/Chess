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
        var moves = new ArrayList<ChessMove>();

        Rule rule = switch (getPieceType()) {
            case BISHOP -> new Rule(true, new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1}});
            case ROOK   -> new Rule(true, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
            case KNIGHT -> new Rule(false, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {-1, 2}, {-1, -2}, {1, 2}, {1, -2}});
            case QUEEN  -> new Rule(true, new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1}});
            case KING   -> new Rule(false, new int[][]{{1, -1}, {-1, 1}, {-1, -1}, {1, 1}});
            default -> null;
        };
//
        if (rule == null){
            return null;
        }
        return rule.getMoves(board, myPosition);


//        return moves;

    }

    public class Rule {

        private boolean multiMove;
        private final int[][] moves;
        public Rule (boolean multiMove, int[][] moves){
            this.multiMove = multiMove;
            this.moves = moves;
        }
        public boolean can_move(int[] move, ChessBoard board, ChessPosition pos){
            if (pos.getRow()+move[0] <= 0 || pos.getRow()+move[0] >= 9 || pos.getColumn()+move[1] <=0 || pos.getColumn()+move[1] >= 9){
                this.multiMove = false;
                return false;
            }
            ChessPosition new_pos = new ChessPosition(pos.getRow()+move[0], pos.getColumn()+move[1]);
            ChessPiece new_piece = board.getPiece(new_pos);
            if (new_piece == null){
                return true;
            }
            this.multiMove = false;
            return new_piece.pieceColor != pieceColor;
        }

        public void addMove(int[] move, ChessBoard board, ChessPosition old_pos, ChessPosition pos, ArrayList<ChessMove> moves){

            if (can_move(move, board, pos)){
                ChessPosition new_pos = new ChessPosition(pos.getRow()+move[0], pos.getColumn()+move[1]);
                ChessMove new_move = new ChessMove(old_pos, new_pos, null);
                board.addPiece(new_pos, board.getPiece(pos));
                board.addPiece(pos, null);
                moves.add(new_move);
                if (multiMove) {
                    addMove(move, board, old_pos,  new_pos, moves);
                }
                else return;
            }
            else return;

        }

        public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos) {
            var moves = new ArrayList<ChessMove>();
            if (this.multiMove) {
                for (int[] move : this.moves){
                    multiMove = true;
                    addMove(move, board, pos, pos, moves);
                }
            }
            else {
                for (int[] move : this.moves){
                    addMove(move, board, pos, pos, moves);
                }
            }

            return moves;
        }
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
