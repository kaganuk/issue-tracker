INSERT INTO DEVELOPER (name) VALUES
    ('Developer 1'),
    ('Developer 2'),
    ('Developer 3'),
    ('Developer 4');

-- Insert stories in estimated status for planning
INSERT INTO ISSUE (title, description, type, status, priority, estimation, assignee_id, created_at) VALUES
    ('Story 1', 'This is a sample issue description 1', 'STORY', 'ESTIMATED', null, 5, null, NOW()),
    ('Story 2', 'This is a sample issue description 2', 'STORY', 'ESTIMATED', null, 8, null, NOW()),
    ('Story 3', 'This is a sample issue description 3', 'STORY', 'ESTIMATED', null, 10, null, NOW()),
    ('Story 4', 'This is a sample issue description 4', 'STORY', 'ESTIMATED', null, 5, null, NOW()),
    ('Story 5', 'This is a sample issue description 5', 'STORY', 'ESTIMATED', null, 5, null, NOW()),
    ('Story 6', 'This is a sample issue description 6', 'STORY', 'ESTIMATED', null, 6, null, NOW()),
    ('Story 7', 'This is a sample issue description 7', 'STORY', 'ESTIMATED', null, 5, null, NOW()),
    ('Story 8', 'This is a sample issue description 8', 'STORY', 'ESTIMATED', null, 3, null, NOW()),
    ('Story 9', 'This is a sample issue description 9', 'STORY', 'ESTIMATED', null, 5, null, NOW()),
    ('Story 10', 'This is a sample issue description 10', 'STORY', 'ESTIMATED', null, 7, null, NOW()),
    ('Story 11', 'This is a sample issue description 11', 'STORY', 'ESTIMATED', null, 5, null, NOW()),
    ('Story 12', 'This is a sample issue description 12', 'STORY', 'ESTIMATED', null, 8, null, NOW()),
    ('Story 13', 'This is a sample issue description 13', 'STORY', 'ESTIMATED', null, 1, null, NOW()),
    ('Story 14', 'This is a sample issue description 14', 'STORY', 'ESTIMATED', null, 4, null, NOW()),
    ('Story 15', 'This is a sample issue description 15', 'STORY', 'ESTIMATED', null, 9, null, NOW());

-- Insert stories in new,completed statuses to show they are not included in planning
 INSERT INTO ISSUE (title, description, type, status, priority, estimation, assignee_id, created_at) VALUES
    ('Story 16', 'This is a sample issue description 16', 'STORY', 'COMPLETED', null, 9, null, NOW()),
    ('Story 17', 'This is a sample issue description 17', 'STORY', 'NEW', null, 9, null, NOW()),
    ('Story 18', 'This is a sample issue description 18', 'STORY', 'COMPLETED', null, 9, null, NOW()),
    ('Story 19', 'This is a sample issue description 19', 'STORY', 'NEW', null, 9, null, NOW()),
    ('Story 20', 'This is a sample issue description 20', 'STORY', 'COMPLETED', null, 9, null, NOW());

-- Insert bugs to show they are not included in planning
INSERT INTO ISSUE (title, description, type, status, priority, estimation, assignee_id, created_at) VALUES
    ('Story 21', 'This is a sample issue description 21', 'BUG', 'NEW', 'CRITICAL', null, null, NOW()),
    ('Story 22', 'This is a sample issue description 22', 'BUG', 'RESOLVED', 'MAJOR', null, null, NOW()),
    ('Story 23', 'This is a sample issue description 23', 'BUG', 'NEW', 'CRITICAL', null, null, NOW()),
    ('Story 24', 'This is a sample issue description 24', 'BUG', 'VERIFIED', 'MINOR', null, null, NOW()),
    ('Story 25', 'This is a sample issue description 25', 'BUG', 'RESOLVED', 'CRITICAL', null, null, NOW());