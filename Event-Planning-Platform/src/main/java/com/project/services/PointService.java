package com.project.services;

import org.springframework.stereotype.Service;
import com.project.entities.Point;
import com.project.entities.User;
import com.project.repository.PointRepository;
import com.project.request.PointAddRequest;
import com.project.request.PointUpdateRequest;

import java.util.List;
import java.util.Optional;

@Service
public class PointService {

    private PointRepository pointRepository;
    private UserService userService;

    public PointService(PointRepository pointRepository, UserService userService) {
        this.pointRepository = pointRepository;
        this.userService = userService;
    }

    public List<Point> getAllPoint()
    {
        return pointRepository.findAll();
    }

    public Point addPoint(PointAddRequest pointAddRequest)
    {
        //siteye kaydolurken kullanılacak
        User user = userService.getUserById(pointAddRequest.getUserId());
        if(user==null)
            return null;
        Point point = new Point();
        point.setId(pointAddRequest.getPointId());
        point.setEarnedDate(pointAddRequest.getEarnedDate());
        point.setScore(pointAddRequest.getScore());
        point.setUser(user);
        return pointRepository.save(point);
    }

    public Point getPointById(Long pointId)
    {
        return pointRepository.findById(pointId).orElse(null);
    }

    public List<Point> getPointByUserId(Optional<Long> userId)
    {
        if(userId.isPresent())
            return pointRepository.findByUserId(userId.get());
        else
            return pointRepository.findAll();
    }

    public Point updatePointById(Long pointId, PointUpdateRequest pointUpdateRequest)
    {
        Optional<Point> point = pointRepository.findById(pointId);
        if(point.isPresent())
        {
            Point foundPoint = point.get();
            foundPoint.setScore(pointUpdateRequest.getScore());
            foundPoint.setEarnedDate(pointUpdateRequest.getEarnedDate());
            return pointRepository.save(foundPoint);
        }
        else
            return null;
    }

    public void deletePointById(Long pointId)
    {
        pointRepository.deleteById(pointId);
    }
}
