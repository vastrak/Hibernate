#REMOVE PREVIOUS DATA. 
DELETE FROM issueitems;
DELETE FROM issues;
DELETE FROM devices;
DELETE FROM technicians;
#TECHNICIANS
INSERT INTO technicians (technician_id, name) VALUES ('1', 'Luis Uno Zelaya');
INSERT INTO technicians (technician_id, name) VALUES ('2', 'José Dos Carrera');
INSERT INTO technicians (technician_id, name) VALUES ('3', 'Arturo Tres López');
#DEVICES
INSERT INTO devices (device_id, deploy, name) VALUES ('1', '2019-03-21 00:00:00', 'Máquina A');
INSERT INTO devices (device_id, deploy, name) VALUES ('2', '2019-04-22 00:00:00', 'Máquina B');
INSERT INTO devices (device_id, deploy, name) VALUES ('3', '2019-04-23 00:00:00', 'Máquina C');
INSERT INTO devices (device_id, deploy, name) VALUES ('4', '2019-05-24 00:00:00', 'Máquina D');
#ISSUES
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('1', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '1', '1');
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('2', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '1', '3');
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('3', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '2', '3');
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('4', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '4', '3');
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('5', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '3', '1');
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('6', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '4', '3');
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('7', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '3', '1');
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('8', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '1', '1');
INSERT INTO issues (issue_id, closed, created, device_id, technician_id) VALUES ('9', '2019-06-12 01:00:00', '2019-06-05 00:00:00', '2', '1');
# ISSUEITEMS
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('1', '0', '0', '0', 'New ussue', '1');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('2', '1', '1', '1', 'New ussue', '1');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('3', '1', '2', '3', 'New ussue', '2');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('4', '0', '0', '2', 'New ussue', '2');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('5', '1', '2', '1', 'New ussue', '3');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('6', '0', '2', '0', 'New ussue', '3');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('7', '0', '1', '0', 'New ussue', '4');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('8', '1', '0', '1', 'New ussue', '4');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('9', '0', '0', '2', 'New ussue', '4');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('10', '0', '0', '2', 'New ussue', '4');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('11', '1', '1', '0', 'New ussue', '5');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('12', '1', '1', '0', 'New ussue', '6');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('13', '1', '1', '0', 'New ussue', '7');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('14', '0', '0', '1', 'New ussue', '8');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('15', '0', '2', '1', 'New ussue', '8');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('16', '0', '2', '3', 'New ussue', '8');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('17', '1', '0', '0', 'New ussue', '9');
INSERT INTO issueitems (issueItem_id, codeType, issueType, levelType, notes, issue_id) VALUES ('18', '0', '1', '0', 'New ussue', '9');