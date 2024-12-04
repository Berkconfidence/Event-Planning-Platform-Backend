package com.project.controllers;

import org.springframework.web.bind.annotation.*;
import com.project.entities.Point;
import com.project.request.PointAddRequest;
import com.project.request.PointUpdateRequest;
import com.project.services.PointService;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/points")
public class PointController {

    private PointService pointService;
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public List<Point> getAllPoint()
    {
        return pointService.getAllPoint();
    }

    @PostMapping
    public Point addPoint(@RequestBody PointAddRequest pointAddRequest)
    {
        return pointService.addPoint(pointAddRequest);
    }

    @GetMapping("/{pointId}")
    public Point getPointById(@PathVariable Long pointId)
    {
        return pointService.getPointById(pointId);
    }

    @GetMapping("/{userId}")
    public List<Point> getPointByUserId(@PathVariable Long userId)
    {
        return pointService.getPointByUserId(Optional.of(userId));
    }

    @PostMapping("/{pointId}")
    public Point updatePointById(@PathVariable Long pointId, @RequestBody PointUpdateRequest pointUpdateRequest)
    {
        return pointService.updatePointById(pointId, pointUpdateRequest);
    }

    @DeleteMapping("/{pointId}")
    public void deletePointById(@PathVariable Long pointId)
    {
        pointService.deletePointById(pointId);
    }
}
