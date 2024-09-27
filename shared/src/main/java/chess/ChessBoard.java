package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;


        this.board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] pieces = new ChessPiece.PieceType[]{ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (int i = 0; i < 8; i++) {
            board[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, pieces[i]);
            board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, pieces[i]);

            board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("\n");
        for (int i = 0; i < 8; i++) {
            ret.append("|");
            for (int j = 0; j < 8; j++) {
                if (this.board[i][j] == null){
                    ret.append(" |");
                }
                else{
                    ret.append(this.board[i][j].toString());
                    ret.append("|");
                }
            }
            ret.append("\n");
        }
        return ret.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(this.board, that.board);
    }

    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

}
