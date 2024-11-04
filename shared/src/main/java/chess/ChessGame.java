package chess;

import java.util.ArrayList;
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
        TeamColor pieceColor = piece.getTeamColor();
        var validMoves = new ArrayList<ChessMove>();


            for (ChessMove move : piece.pieceMoves(this.board, startPosition)) {
                ChessBoard boardCopy = this.board.deepCopy();
                boardCopy.addPiece(move.getEndPosition(), piece);
                boardCopy.addPiece(move.getStartPosition(), null);
                if (!isBoardInCheck(pieceColor, boardCopy)) {
                    validMoves.add(move);
                }
            }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition currentPos = move.getStartPosition();
        ChessPiece piece = board.getPiece(currentPos);
        ChessPosition endPos = move.getEndPosition();
        ChessPiece endPiece = board.getPiece(endPos);
        if (piece == null){
            throw new InvalidMoveException("there is not piece there");
        }
        if (piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException("Piece color != team turn");
        }
        if (endPiece!=null && endPiece.getTeamColor()==piece.getTeamColor()) {
            throw new InvalidMoveException("Cannot move to occupied space of same team");
        }
        if(validMoves(move.getStartPosition()).isEmpty()){
            throw new InvalidMoveException("This piece has no valid moves");
        }
        if(!validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException("Cant move there");
        }

        if (move.getPromotionPiece() != null){
            board.addPiece(endPos, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        }
        else {
            board.addPiece(endPos, piece);
        }
        board.addPiece(currentPos, null);
        if (teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        }
        else{
            teamTurn = TeamColor.WHITE;
        }

    }

    public ChessPosition findKing(TeamColor teamColor, ChessBoard board ){
        ChessPosition kingPos;

        for (int row=0; row<8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row + 1, col + 1));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor) {
                    kingPos = new ChessPosition(row + 1, col + 1);
                    return kingPos;
                }
            }
        }
        return null;
    }

    public boolean isBoardInCheck(TeamColor teamColor, ChessBoard board){
        ChessPosition kingPos = findKing(teamColor, board);

        if (kingPos != null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    ChessPiece enemyPiece = board.getPiece(new ChessPosition(i + 1, j + 1));
                    if (enemyPiece != null && enemyPiece.getTeamColor() != teamColor) {
                        var moves = enemyPiece.pieceMoves(board, new ChessPosition(i + 1, j + 1));
                        for (ChessMove move : moves) {
                            if (move.getEndPosition().getColumn() == kingPos.getColumn() && move.getEndPosition().getRow() == kingPos.getRow()) {
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
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isBoardInCheck(teamColor, this.board);
    }


    public boolean checkValidMoves(TeamColor teamColor){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece friendPiece = board.getPiece(new ChessPosition(i + 1, j + 1));
                if (friendPiece != null && friendPiece.getTeamColor() == teamColor){
                    var moves = validMoves(new ChessPosition(i + 1, j + 1));
                    if (!moves.isEmpty()){
                        return false;
                    }
                }

            }
        }
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)){

            return checkValidMoves(teamColor);
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheckmate(teamColor)){
            return false;
        }

        return checkValidMoves(teamColor);
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
