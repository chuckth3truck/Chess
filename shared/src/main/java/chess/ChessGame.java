package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece != null){
            return piece.pieceMoves(this.board, startPosition);

        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition current_pos = move.getStartPosition();
        ChessPiece piece = board.getPiece(current_pos);
        ChessPosition end_pos = move.getEndPosition();
        ChessPiece end_piece = board.getPiece(end_pos);

        if (piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("Piece color != team turn");
        } else if (end_piece!=null && end_piece.getTeamColor()==piece.getTeamColor()) {
            throw new InvalidMoveException("Cannot move to occupied space of same team");
        }


        board.addPiece(end_pos, piece);
        board.addPiece(current_pos, null);

    }

    public ChessPosition findKing(TeamColor teamColor){
        ChessPosition king_pos;

        for (int row=0; row<8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row + 1, col + 1));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor) {
                    king_pos = new ChessPosition(row + 1, col + 1);
                    return king_pos;
                }
            }
        }
        return null;
    }

    public boolean inWay(ChessPosition pos){


        return false;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king_pos = findKing(teamColor);

        if (king_pos != null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPiece enemyPiece = board.getPiece(new ChessPosition(i + 1, j + 1));
                    if (enemyPiece != null && enemyPiece.getTeamColor() != teamColor) {
                        var moves = enemyPiece.pieceMoves(board, new ChessPosition(i + 1, j + 1));
                        for (ChessMove move : moves) {
                            if (move.getEndPosition() == king_pos) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            ChessPosition king_pos = findKing(teamColor);
            if (king_pos != null) {
                ChessPiece piece = board.getPiece(king_pos);
                var kingMoves = piece.pieceMoves(board, king_pos);
                if (kingMoves.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
