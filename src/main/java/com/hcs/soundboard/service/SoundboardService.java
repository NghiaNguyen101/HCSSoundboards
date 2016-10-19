package com.hcs.soundboard.service;

import com.hcs.soundboard.data.Board;
import com.hcs.soundboard.data.HCSUser;
import com.hcs.soundboard.data.SoundFile;
import com.hcs.soundboard.db.SoundboardDAO;
import com.hcs.soundboard.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Handles sound- and soundboard-related operations.
 */
@Service
public class SoundboardService {
    @Autowired
    private SoundboardDAO soundboardDao;

    /**
     * Gets the sound file for a sound id.
     * @param soundId The boardId of the sound in question.
     * @return The sound's file.
     * @throws IOException Shouldn't happen.
     */
    public SoundFile getSound(int soundId) throws IOException {
        return soundboardDao.getSoundFile(soundId);
    }

    /**
     * Gets a board for viewing. Checks to make sure the user is authorized
     * to view the board.
     * @param user The user trying to view the board.
     * @param boardId The boardId of the board.
     * @return The board (with sounds populated) if the user is authorized.
     */
    public Board getBoardForViewing(HCSUser user, int boardId) {
        Board board = soundboardDao.getBoard(boardId, false, true);
        if (!canViewBoard(user, board)) {
            throw new ForbiddenException();
        }
        return board;
    }

    public Board getBoardForPreviewing(HCSUser user, int boardId) {
        Board board = soundboardDao.getBoard(boardId, true, false);
        if (!canEditBoard(user, board)) {
            throw new ForbiddenException();
        }
        return board;
    }

    public void shareBoard(HCSUser user, int boardId) {
        Board board = soundboardDao.getBoard(boardId, false, false);
        if (!canEditBoard(user, board)) {
            throw new ForbiddenException();
        }
        soundboardDao.shareBoard(boardId);
    }

    /**
     * Gets a board for editing. Checks to make sure the user is authorized
     * to edit the board.
     * @param user The user trying to edit the board.
     * @param boardId The id of the board.
     * @return The board (with sounds populated) if the user is authorized.
     */
    public Board getBoardForEditing(HCSUser user, int boardId) {
        Board board = soundboardDao.getBoard(boardId, true, false);
        if (!canEditBoard(user, board)) {
            throw new ForbiddenException();
        }
        return board;
    }

    /**
     * Gets metadata for all of a user's boards. Currently doesn't check to
     * make sure the user is authorized to view or edit any of the boards
     * since this is only called for the currently logged-in user who
     * always has access to their own boards.
     * @param user The user whose boards to get
     * @return List of all the user's boards (sounds are not populated)
     */
    public List<Board> getUsersBoards(HCSUser user) {
        return soundboardDao.getUsersBoards(user.getUsername());
    }

    public List<Board> getAllBrowsableBoards() {
        return soundboardDao.getAllBrowsableBoards();
    }

    public List<Board> getUsersPublicBoards(String username) {
        return soundboardDao.getUsersPublicBoards(username);
    }

    /**
     * Adds the sounds to the board. Throws if the user is not authorized to edit the board.
     * @param user The user adding the sounds to the board.
     * @param sounds The sounds to add
     * @param names The names of the sounds
     * @param boardId The id of the board in question.
     * @return List of the ids of the new sounds.
     */
    public List<Integer> addSoundsToBoard(HCSUser user, List<SoundFile> sounds, List<String> names, int boardId) {
        Board board = soundboardDao.getBoard(boardId, false, false);
        if (canEditBoard(user, board)) {
            return soundboardDao.addSoundsToBoard(sounds, names, boardId);
        }
        throw new ForbiddenException();
    }

    /**
     * Creates a new soundboard
     * @param user The user creating the soundboard.
     * @return The id of the new board.
     */
    public int createSoundboard(HCSUser user, String title, String description) {
        if (!user.isAnonymous()) {
            return soundboardDao.createSoundboard(user.getUsername(), title, description);
        }
        throw new ForbiddenException();
    }

    private boolean canViewBoard(HCSUser user, Board board) {
        return board.hasBeenShared() || canEditBoard(user, board);
    }

    private boolean canEditBoard(HCSUser user, Board board) {
        return user.isAdmin() || board.getOwnerName().equals(user.getUsername());
    }
}