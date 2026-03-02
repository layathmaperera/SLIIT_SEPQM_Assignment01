import React, { useState, useEffect } from 'react';
import { getStudents, createStudent, deleteStudent } from './api';

function App() {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    age: '',
    course: ''
  });

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    try {
      const data = await getStudents();
      setStudents(data);
    } catch (error) {
      console.error('Error loading students', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const newStudent = await createStudent({
        ...formData,
        age: parseInt(formData.age, 10)
      });
      setStudents(prev => [...prev, newStudent]);
      setFormData({ name: '', email: '', age: '', course: '' }); // Reset
    } catch (error) {
      console.error('Failed to create student', error);
      alert('Failed to save student. Ensure backend is running!');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this student?")) return;

    try {
      await deleteStudent(id);
      setStudents(prev => prev.filter(s => s.id !== id));
    } catch (error) {
      console.error('Failed to delete', error);
    }
  };

  return (
    <div className="container">
      <header className="header">
        <h1>Student Portal</h1>
        <p>Manage University Enrollments</p>
      </header>

      <main className="dashboard-layout">
        <section className="card form-section">
          <h2>Add New Student</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Full Name</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="Jane Doe"
                required
              />
            </div>
            <div className="form-group">
              <label>Email Address</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="jane@university.edu"
                required
              />
            </div>
            <div className="form-group">
              <label>Age</label>
              <input
                type="number"
                name="age"
                value={formData.age}
                onChange={handleChange}
                placeholder="21"
                min="16" max="100"
                required
              />
            </div>
            <div className="form-group">
              <label>Course Name</label>
              <input
                type="text"
                name="course"
                value={formData.course}
                onChange={handleChange}
                placeholder="SE3010 - Software Testing"
                required
              />
            </div>
            <button type="submit" className="btn btn-primary" disabled={submitting}>
              {submitting ? 'Enrolling...' : 'Enroll Student'}
            </button>
          </form>
        </section>

        <section className="card list-section">
          <h2>Enrolled Students ({students.length})</h2>

          {loading ? (
            <div className="loading">Loading records...</div>
          ) : students.length === 0 ? (
            <div className="empty-state">
              <p>No students enrolled yet.</p>
              <p>Add a student using the form to get started.</p>
            </div>
          ) : (
            <div className="student-list">
              {students.map(student => (
                <div key={student.id} className="student-card">
                  <div className="student-info">
                    <h3>{student.name}</h3>
                    <p>{student.email} • Age: {student.age}</p>
                    <span className="badge">{student.course}</span>
                  </div>
                  <button onClick={() => handleDelete(student.id)} className="btn btn-danger">
                    Remove
                  </button>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;
