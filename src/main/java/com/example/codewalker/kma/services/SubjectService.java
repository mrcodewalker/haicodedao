package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Subject;
import com.example.codewalker.kma.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService implements ISubjectService {
    private final SubjectRepository subjectRepository;

//    @Override
//    public boolean findBySubjectName(String subjectName) {
//        if (subjectRepository.findBySubjectName(subjectName)!=null){
//            return true;
//        }
//        return false;
//    }

    @Override
    public Subject createSubject(Subject subject) {
        if (subjectRepository.findFirstBySubjectName(subject.getSubjectName())!=null){
            return null;
        }
        return subjectRepository.save(subject);
    }

    @Override
    public Subject findSubjectByName(String subjectName) {
        return subjectRepository.findFirstBySubjectName(subjectName);
    }

    @Override
    public Subject findBySubjectId(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()){
            return subject.get();
        }
        return null;
    }

    @Override
    public boolean existBySubjectName(String subjectName) {
        return subjectRepository.existBySubjectName(subjectName);
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject findBySubjectName(String subjectName) {
        List<Subject> list = this.subjectRepository.findAll();
        for (Subject clone: list){
            if(clone.getSubjectName().equalsIgnoreCase(subjectName)
        || Normalizer.normalize(clone.getSubjectName(), Normalizer.Form.NFD).replaceAll("\\p{M}", "").equalsIgnoreCase(
                    Normalizer.normalize(subjectName, Normalizer.Form.NFD).replaceAll("\\p{M}", ""))){
                return clone;
            }
        }
        return null;
    }
}
