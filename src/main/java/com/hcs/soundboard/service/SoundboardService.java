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

@Service
public class SoundboardService {
    @Autowired
    private SoundboardDAO soundboardDao;

    public SoundFile getSound(int soundId) throws IOException {
        return soundboardDao.getSoundFile(soundId);
    }

    public Board getBoardForViewing(HCSUser user, int boardId) {
        Board board = soundboardDao.getBoard(boardId, true);
        if (!canViewBoard(user, board)) {
            throw new ForbiddenException();
        }
        return board;
    }

    public Board getBoardForEditing(HCSUser user, int boardId) {
        Board board = soundboardDao.getBoard(boardId, true);
        if (!canEditBoard(user, board)) {
            throw new ForbiddenException();
        }
        return board;
    }

    public List<Board> getUsersBoards(HCSUser user) {
        return soundboardDao.getUsersBoards(user.getUsername());
    }

    public List<Integer> addSoundsToBoard(HCSUser user, List<SoundFile> sounds, List<String> names, int boardId) {
        Board board = soundboardDao.getBoard(boardId, false);
        if (canEditBoard(user, board)) {
            return soundboardDao.addSoundsToBoard(sounds, names, boardId);
        }
        throw new ForbiddenException();
    }

    public int createSoundboard(HCSUser user) {
        if (!user.isAnonymous()) {
            return soundboardDao.createSoundboard(user.getUsername());
        }
        throw new ForbiddenException();
    }

    private boolean canViewBoard(HCSUser user, Board board) {
        return board.isPublic() || canEditBoard(user, board);
    }

    private boolean canEditBoard(HCSUser user, Board board) {
        return user.isAdmin() || board.getOwnerName().equals(user.getUsername());
    }
}