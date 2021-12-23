package com.example.pandemikent.Controller;

import com.example.pandemikent.Model.Section;
import com.example.pandemikent.Model.Student;
import com.example.pandemikent.Model.Class;
import com.example.pandemikent.Model.Instructor;
import com.example.pandemikent.Model.MakeUpExam;
import com.example.pandemikent.Model.MakeUpSession;
import com.example.pandemikent.Service.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassController {
  @Autowired
  private ClassService classService;
  
  @Autowired
  private MakeUpService makeUpService;

  // done
  @GetMapping("/getClasses")
  public @ResponseBody List<String> displayClasses(@RequestParam("userId") String userId) {
	  List<String> classes = classService.listUserClasses(userId);
	  // theModel.addAttribute("classes", classes);
	  return classes;
  }
  
//   @GetMapping("/sections")
//   public String displaySections(@RequestParam("userId") String userId, @RequestParam("classId") String classId, Model theModel) {
// 	  List<Section> sections = classService.listUserSections(userId, classId);
// 	  theModel.addAttribute("sections", sections);
// 	  return "listSections";
//   }
  
  // where is it getting this information from ??????
//   @GetMapping("/coursePage")
//   public String displayCoursePage(@RequestParam("userId") String userId, @RequestParam("classId") String classId, @RequestParam("sectionId") Long sectionId, Model theModel) {
// 	  theModel.addAttribute("classId", classId);
// 	  theModel.addAttribute("sectionId", sectionId);
// 	  Instructor instr = classService.getSectionInstr(sectionId);
// 	  theModel.addAttribute("instr", instr);
// 	  Boolean accessStatus = classService.getUserAccess(userId);
// 	  theModel.addAttribute("accessStatus", accessStatus);
// 	  List<Student> participants = classService.listParticipants(sectionId);
// 	  theModel.addAttribute("participants", participants);
// 	  return "coursePage";
//   }
  
  @GetMapping("/sectionPage")
  public String displaySectionPage(@RequestParam("classId") String classId, @RequestParam("sectionId") Long sectionId, @RequestParam("instrId") String instrId, Model theModel) {
	  theModel.addAttribute("classId", classId);
	  theModel.addAttribute("instrId", instrId);
	  List<Student> participants = classService.listParticipants(sectionId);
	  theModel.addAttribute("participants", participants);
	  return "sectionPage";
  }
  
  @GetMapping("/addClassPage")
  public String displayAddClassPage(@RequestParam("instrId")String instrId, Model theModel) {
	  Class newClass = new Class();
	  theModel.addAttribute("newClass", newClass);
	  theModel.addAttribute("instrId", instrId);
	  return "addClass";
  }
  
  // done
  @PostMapping("/addClass")
  public @ResponseBody String addClass(@RequestParam String newClass, @RequestParam String section, 
  								@RequestParam String instr) {
	  Class c = classService.save(newClass, section, instr);

	  if(c == null) {
		  return "alpha";
	  } else {
		  // return "redirect:displayClasses";
		  return c.toString();
	  }
  }


  @GetMapping("/joinClassPage")
  public String displayJoinClassPage(@RequestParam("userId") String userId,
  									 @RequestParam String className, Model theModel) {
	  Class joinClass = new Class();
	  Section joinSection = new Section();
	  theModel.addAttribute("joinClass", joinClass);
	  theModel.addAttribute("joinSection", joinSection);
	  theModel.addAttribute("userId", userId);
	  return "joinClass";
  }
  
  @PostMapping("/joinClass")
  public @ResponseBody Student joinClass(@ModelAttribute("joinClass") String joinClass, @ModelAttribute("joinSection") String joinSection, @ModelAttribute("userId") String userId) {
	  Student s = classService.joinClass(joinClass, userId);
	  return s;
	//   if(b) {
	// 	  return "redirect:displayClasses";
	//   } else {
	// 	  return "errorPage";
	//   }
  }
  
  @GetMapping("/participants")
  public String displayParticipantsPage(@ModelAttribute("participants") ArrayList<Student> participants, Model theModel) {
	  theModel.addAttribute("participants", participants);
	  return "participantsPage";
  }
  
  @GetMapping("/makeUpSessionPage")
  public String displayMakeUpSessionPage(@ModelAttribute("sectionId") Long sectionId, Model theModel) {
	  ArrayList<MakeUpSession> makeUpSessions = makeUpService.getClassMakeUpSessions(sectionId);	
	  theModel.addAttribute("makeUpSessions", makeUpSessions);
	  return "makeUpSessionsPage";
  }
  
  @GetMapping("/makeUpExamPage")
  public String displayMakeUpExamPage(@ModelAttribute("classId") String classId, Model theModel) {
	  MakeUpExam makeUpExam = makeUpService.getClassMakeUpExam(classId);	
	  theModel.addAttribute("makeUpExam", makeUpExam);
	  return "makeUpExamPage";
  }
  
  public String displayMissedClassesPage() {
	  return null;
  }
  
  @GetMapping("/seeStudents")
  public String displaySeeStudentsPage(@ModelAttribute("participants") ArrayList<Student> participants, Model theModel) {
	  theModel.addAttribute("participants", participants);
	  return "seeStudentsPage";
  }
  
  @GetMapping("/quarantinedStudents")
  public String displayQuarantinedStudentsPage(@ModelAttribute("classId") String classId, @ModelAttribute("instrId") String instrId) {
	  List<Student> quarantinedStudents = classService.listQuarantinedStudents(classId, instrId);
	  return "quarantinedStudents";
  }
  
  @GetMapping("/setMakeUpExamPage")
  public String displaySetMakeUpExamPage(@ModelAttribute("classId") String classId, Model theModel) {
	  MakeUpExam mue = new MakeUpExam();
	  theModel.addAttribute("makeUpExam", mue);
	  theModel.addAttribute("classId", classId);
	  return "addMakeUpExam";
  }
  
//   @GetMapping("/setMakeUpExam")
//   public String setMakeUpExam(@ModelAttribute("makeUpExam") MakeUpExam makeUpExam, @ModelAttribute("classId") String classId) {
// 	  makeUpService.setMakeUpExam(makeUpExam, classId);
// 	  return "redirect:displaySectionPage";
//   }
  
  @GetMapping("/setMakeUpSessionPage")
  public String displaySetMakeUpSessionPage(@ModelAttribute("sectionId") Long sectionId, Model theModel) {
	  MakeUpSession mus = new MakeUpSession();
	  theModel.addAttribute("makeUpSession", mus);
	  theModel.addAttribute("sectionId", sectionId);
	  return "addMakeUpSession";
  }
  
  @GetMapping("/setMakeUpSession")
  public String setMakeUpSession(@ModelAttribute("makeUpSessions") MakeUpSession makeUpSession, @ModelAttribute("sectionId") Long sectionId) {
	  makeUpService.setMakeUpSession(makeUpSession, sectionId);
	  return "redirect:displaySectionPage";
  }
}