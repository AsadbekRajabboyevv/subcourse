package uz.asadbek.course.service;

import org.springframework.stereotype.Service;
import uz.asadbek.course.domain.Science;
import uz.asadbek.course.model.ScienceDTO;
import uz.asadbek.course.repos.ScienceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScienceService {
    private final ScienceRepository scienceRepository;

    public ScienceService(ScienceRepository scienceRepository) {
        this.scienceRepository = scienceRepository;
    }


    public List<ScienceDTO> getAllSciences() {
        return scienceRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private ScienceDTO mapToDTO(Science science) {
        ScienceDTO dto = new ScienceDTO();
        dto.setId(science.getId());
        dto.setName(science.getName());
        return dto;
    }

    public Science findById(Long scienceId) {
        return scienceRepository.findById(scienceId).orElse(null);
    }
}
