/**
 * ****************************************************************************
 * select operates
 * ****************************************************************************
 */

/**
 * show select project dialog
 * 
 * @param $inp
 */
function showProject($inp) {
	loadProjectData($inp);
	$("#projectModal").dialog("open");
}

/**
 * show select multi projects dialog
 * 
 * @param $inp
 */
function showProjects($inp) {
	loadProjectData($inp);
	$("#projectsModal").dialog("open");
}

/**
 * show select status dialog
 * 
 * @param $imp
 */
function showStatuses($inp) {
	initStatus($inp);
	$("#statusModal").dialog("open");
}

/**
 * show select level dialog
 * 
 * @param $inp
 */
function showLevels($inp) {
	initLevel($inp);
	$("#levelModal").dialog("open");
}

/**
 * show select priority dialog
 * 
 * @param $inp
 */
function showPriorities($inp) {
	initPriority($inp);
	$("#priorityModal").dialog("open");
}
