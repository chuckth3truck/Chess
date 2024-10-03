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

        Rule rule = switch (getPieceType()){
            case KING -> new Rule(false, new int[][]{{1,1}, {-1, -1}, {-1, 1}, {1, -1},
                    {1,0}, {-1, 0}, {0, 1}, {0, -1}});
            case QUEEN -> new Rule(true, new int[][]{{1,1}, {-1, -1}, {-1, 1}, {1, -1},
                    {1,0}, {-1, 0}, {0, 1}, {0, -1}});
            case BISHOP -> new Rule(true, new int[][]{{1,1}, {-1, -1}, {-1, 1}, {1, -1}});
            case KNIGHT -> new Rule(false, new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                    {1, 2}, {1, -2}, {-1, 2}, {-1, -2}});

            case ROOK -> new Rule(true, new int[][]{{1,0}, {-1, 0}, {0, 1}, {0, -1}});
            case PAWN -> new pawnRule(false, new int[][]{{1, 0}, {-1, 0}, {1, -1}, {1, 1}, {-1, 1}, {-1, -1}});
        };

        ChessBoard boardCopy = board.deepCopy();
        return rule.getMoves(boardCopy, myPosition);

    }

    public class Rule {
        private boolean multiMove;
        private final int[][] movSet;
        public Rule(boolean multiMove, int[][] moveSet){
            this.multiMove = multiMove;
            this.movSet = moveSet;

        }
        public boolean canMove(ChessBoard board, ChessPosition pos, int[] move){
            if (pos.getRow()+move[0] >= 9 || pos.getRow()+move[0] <= 0 || pos.getColumn()+move[1] >= 9 || pos.getColumn()+move[1] <= 0){
                multiMove = false;
                return false;
            }
            ChessPosition new_pos = new ChessPosition(pos.getRow()+move[0], pos.getColumn()+move[1]);
            ChessPiece new_piece = board.getPiece(new_pos);

            if(new_piece == null){
                return true;
            }

            multiMove=false;
            return new_piece.pieceColor != pieceColor;
        }

        public void makeMove(ChessBoard board, ChessPosition start_pos, ChessPosition pos, int[] move, ArrayList<ChessMove> moves){
            if (canMove(board, pos, move)){
                ChessPosition new_pos = new ChessPosition(pos.getRow()+move[0], pos.getColumn()+move[1]);
                ChessMove new_move = new ChessMove(start_pos, new_pos, null);
                board.addPiece(new_pos, board.getPiece(pos));
                board.addPiece(pos, null);
                moves.add(new_move);

                if (multiMove){
                    makeMove(board, start_pos, new_pos, move, moves);
                }
            }
        }

        public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos){
            var moves = new ArrayList<ChessMove>();
            if (multiMove){
                for (int[] move: this.movSet){
                    multiMove = true;
                    makeMove(board, pos, pos, move, moves);
                }
            }
            else{
                for (int[] move: this.movSet){
                    makeMove(board, pos, pos, move, moves);
                }
            }

            return moves;
        }
    }

    public class pawnRule extends Rule{
        private final int[][] movSet;
        public pawnRule(boolean multiMove, int[][] moveSet){
            super(multiMove, moveSet);
            this.movSet = moveSet;
        }

        public boolean canMove(ChessBoard board, ChessPosition pos, int[] move) {
            if (pos.getRow() + move[0] >= 9 || pos.getRow() + move[0] <= 0 || pos.getColumn() + move[1] >= 9 || pos.getColumn() + move[1] <= 0) {
                return false;
            }

            if (move[0] == -1 && pieceColor == ChessGame.TeamColor.WHITE) {
                return false;
            }
            if (move[0] == 1 && pieceColor == ChessGame.TeamColor.BLACK) {
                return false;
            }

            ChessPosition new_pos = new ChessPosition(pos.getRow() + move[0], pos.getColumn() + move[1]);
            ChessPiece new_piece = board.getPiece(new_pos);

            if ((new_piece != null) && move[1] != 0 && new_piece.pieceColor != pieceColor) {
                return true;
            } else if ((new_piece == null) && move[1] != 0) {
                return false;
            }

            return new_piece == null;
        }

        public boolean checkPromotion(ChessPosition pos, ChessBoard board){
            if (board.getPiece(pos) == null){
                return false;
            }
            return (pos.getRow() == 7 && pieceColor == ChessGame.TeamColor.WHITE ||
                    pos.getRow() == 2 && pieceColor == ChessGame.TeamColor.BLACK);
        }

        @Override
        public void makeMove(ChessBoard board, ChessPosition start_pos, ChessPosition pos, int[] move, ArrayList<ChessMove> moves) {
            if (canMove(board, pos, move)){
                ChessPosition new_pos = new ChessPosition(pos.getRow()+move[0], pos.getColumn()+move[1]);
                if (checkPromotion(pos, board)){
                    moves.add(new ChessMove(start_pos, new_pos, PieceType.KNIGHT));
                    moves.add(new ChessMove(start_pos, new_pos, PieceType.BISHOP));
                    moves.add(new ChessMove(start_pos, new_pos, PieceType.QUEEN));
                    moves.add(new ChessMove(start_pos, new_pos, PieceType.ROOK));
                    return;
                }
                ChessMove new_move = new ChessMove(start_pos, new_pos, null);
                board.addPiece(new_pos, board.getPiece(pos));
                board.addPiece(pos, null);
                moves.add(new_move);

                if (pos.getRow() == 2 && pieceColor == ChessGame.TeamColor.WHITE||
                        pos.getRow() == 7 && pieceColor == ChessGame.TeamColor.BLACK){
                    makeMove(board, start_pos, new_pos, move, moves);
                }
            }
        }

        public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition pos){
            var moves = new ArrayList<ChessMove>();
            for (int[] move: this.movSet){
                makeMove(board, pos, pos, move, moves);
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
