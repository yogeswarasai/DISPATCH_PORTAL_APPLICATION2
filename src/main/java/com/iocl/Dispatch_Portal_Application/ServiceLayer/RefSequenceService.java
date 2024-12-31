package com.iocl.Dispatch_Portal_Application.ServiceLayer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iocl.Dispatch_Portal_Application.Entity.RefSequence;
import com.iocl.Dispatch_Portal_Application.Repositaries.RefSequenceRepository;

@Service
public class RefSequenceService {

	@Autowired
    private RefSequenceRepository refSequenceRepository;
	 
	 @Autowired
	 private MstLocationService locationService;

    public List<RefSequence> findAll() {
        return refSequenceRepository.findAll();
    }



    public Page<RefSequence> findAll(int page, int size) {
        // Sort by createdDate or id in descending order
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "locCode")); 

        Page<RefSequence> refSequences = refSequenceRepository.findAll(pageable);

        // Modify each RefSequence entity to format the locCode field for display
        List<RefSequence> modifiedSequences = refSequences.getContent().stream().map(refSequence -> {
            String locName = locationService.getLocNameByCode(refSequence.getLocCode());
            // Format as 'LocationName (LocationCode)'
            refSequence.setLocCode(
                locName != null && !locName.trim().isEmpty()
                    ? locName + " (" + refSequence.getLocCode() + ")"
                    : refSequence.getLocCode() // If name is unavailable, just display the code
            );
            return refSequence;
        }).toList();

        // Return a new Page object with the modified content
        return new PageImpl<>(modifiedSequences, pageable, refSequences.getTotalElements());
    }

    public Optional<RefSequence> findById(String locCode) {
        return refSequenceRepository.findById(locCode);
    }



    @Transactional
    public RefSequence save(RefSequence refSequence) {
        if (refSequenceRepository.existsByLocCode(refSequence.getLocCode())) {
            return null; // Indicate that the locCode already exists
        }
        return refSequenceRepository.save(refSequence);
    }
    public void deleteById(String locCode) {
        refSequenceRepository.deleteById(locCode);
    }

}
