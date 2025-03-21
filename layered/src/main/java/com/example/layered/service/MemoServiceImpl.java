package com.example.layered.service;

import com.example.layered.dto.MemoRequestDto;
import com.example.layered.dto.MemoResponseDto;
import com.example.layered.entity.Memo;
import com.example.layered.repository.MemoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MemoServiceImpl implements MemoService {

    private final MemoRepository memoRepository;

    public MemoServiceImpl(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    @Override
    public MemoResponseDto saveMemo(MemoRequestDto dto) {

        // 요청받은 데이터로 Memo 객체 생성, ID 없음
        Memo memo = new Memo(dto.getTitle(), dto.getContents());
        return memoRepository.saveMemo(memo);
    }

    @Override
    public List<MemoResponseDto> findAllMemos() {
        return memoRepository.findAllMemos();
    }

    @Override
    public MemoResponseDto findMemoById(Long id) {
//        Optional<Memo> optionalMemo = memoRepository.findMemoById(id);
//
//        if (optionalMemo.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
//        }
//        return new MemoResponseDto(optionalMemo.get());

        Memo memo = memoRepository.findMemoByIdOrElseThrow(id);
        return new MemoResponseDto(memo);
    }

    @Transactional
    @Override
    public MemoResponseDto updateMemo(Long id, String title, String contents) {

        if (title == null || contents == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
        }

        int updatedRow = memoRepository.updateMemo(id, title, contents);

        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        Memo memo = memoRepository.findMemoByIdOrElseThrow(id);
        return new MemoResponseDto(memo);

//        Optional<Memo> optionalMemo = memoRepository.findMemoById(id);
//        return new MemoResponseDto(optionalMemo.get());

    }

    @Transactional
    @Override
    public MemoResponseDto updateTitle(Long id, String title, String contents) {

        if (title == null || contents != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title is a required values.");
        }

        int updatedTitleRow = memoRepository.updateTitle(id, title);

        if (updatedTitleRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
        Memo memo = memoRepository.findMemoByIdOrElseThrow(id);
        return new MemoResponseDto(memo);
//        Optional<Memo> optionalMemo = memoRepository.findMemoById(id);

//        return new MemoResponseDto(optionalMemo.get());
    }

    @Override
    public void deleteMemo(Long id) {

        int deletedRow = memoRepository.deleteMemo(id);

        if (deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

    }
}
