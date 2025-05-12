// LearningPlan.tsx
import React, { useState, useEffect } from 'react';
import { useWebSocket } from "../ws/WebSocketContextProvider";
import { request } from "../../utils/api";
import './LearningPlan.scss';
import { AuthenticationContextProvider, useAuthentication } from '../authentication/contexts/AuthenticationContextProvider';

interface LearningPlan {
  id: string;
  title: string;
  description: string;
  status: 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED';
  startDate: string;
  endDate: string;
}

const LearningPlan: React.FC = () => {
  const [plans, setPlans] = useState<LearningPlan[]>([]);
  const [selectedPlan, setSelectedPlan] = useState<LearningPlan | null>(null);
  const [isCreating, setIsCreating] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState<Partial<LearningPlan>>({});
  const ws = useWebSocket();
  const { user  } = useAuthentication()

   // Fetch all plans on mount
  useEffect(() => {
    request({
      endpoint: "/api/v1/plans",
      onSuccess: (data) => setPlans(data as LearningPlan[]),
      onFailure: (error) => console.error(error),
    });
  }, []);

  // Listen for real-time updates via WebSocket (optional, for multi-user sync)
  useEffect(() => {
    if (!ws) return;
    const subscription = ws.subscribe("/topic/learning-plans", (message) => {
      const { action, plan } = JSON.parse(message.body) as { action: string; plan: LearningPlan };
      setPlans((prev) => {
        switch (action) {
          case "create":
            return [...prev, plan];
          case "update":
            return prev.map((p) => (p.id === plan.id ? plan : p));
          case "delete":
            return prev.filter((p) => p.id !== plan.id);
          default:
            return prev;
        }
      });
    });
    return () => subscription.unsubscribe();
  }, [ws]);

  // CREATE
  const handleCreate = async () => {
    await request<LearningPlan>({
      endpoint: "/api/v1/plans",
      method: "POST",
      body: JSON.stringify(formData),
      contentType: "application/json",
      onSuccess: (plan) => setPlans((prev) => [...prev, plan]),
      onFailure: (error) => console.error(error),
    });
    setIsCreating(false);
    setFormData({});
  };

  // UPDATE
  const handleUpdate = async () => {
    if (!selectedPlan) return;
    alert(selectedPlan.id)
    await request<LearningPlan>({
      endpoint: `/api/v1/plans/${selectedPlan.id}?userId=${user?.id}`,
      method: "PUT",
      body: JSON.stringify({ ...selectedPlan, ...formData }),
      contentType: "application/json",
      onSuccess: (plan) =>
        setPlans((prev) => prev.map((p) => (p.id === plan.id ? plan : p))),
      onFailure: (error) => console.error(error),
    });
    setIsEditing(false);
    setSelectedPlan(null);
    setFormData({});
  };

  // DELETE
  const handleDelete = async (id: string) => {
    await request({
      endpoint: `/api/v1/plans/${id}?userId=${user?.id}`,
      method: "DELETE",
      onSuccess: () => setPlans((prev) => prev.filter((p) => p.id !== id)),
      onFailure: (error) => console.error(error),
    });
    window.location.reload();
  };
  return (
    <div className="learning-plan-container">
      <div className="sidebar">
        <h3>Learning Plans</h3>
        <div className="stats">
          <span>Total Plans:</span>
          <span className="count">{plans.length}</span>
        </div>
        <button 
          className="create-button"
          onClick={() => {
            setIsCreating(true);
            setIsEditing(false);
            setSelectedPlan(null);
          }}
        >
          Create New Plan
        </button>
      </div>

      <div className="main-content">
        {isCreating || isEditing ? (
          <div className="plan-form">
            <h2>{isEditing ? 'Edit Plan' : 'Create Plan'}</h2>
            <input
              placeholder="Title"
              value={formData.title || ''}
              onChange={(e) => setFormData({...formData, title: e.target.value})}
            />
            <textarea
              placeholder="Description"
              value={formData.description || ''}
              onChange={(e) => setFormData({...formData, description: e.target.value})}
            />
            <div className="date-inputs">
              <input
                type="date"
                value={formData.startDate || ''}
                onChange={(e) => setFormData({...formData, startDate: e.target.value})}
              />
              <input
                type="date"
                value={formData.endDate || ''}
                onChange={(e) => setFormData({...formData, endDate: e.target.value})}
              />
            </div>
            <div className="form-actions">
              <button onClick={() => {
                setIsCreating(false);
                setIsEditing(false);
                setFormData({});
              }}>
                Cancel
              </button>
              <button onClick={isEditing ? handleUpdate : handleCreate}>
                {isEditing ? 'Update' : 'Create'}
              </button>
            </div>
          </div>
        ) : selectedPlan ? (
          <div className="plan-detail">
            <h2>{selectedPlan.title}</h2>
            <p>{selectedPlan.description}</p>
            <div className="plan-meta">
              <span>Status: {selectedPlan.status}</span>
              <span>Start: {new Date(selectedPlan.startDate).toLocaleDateString()}</span>
              <span>End: {new Date(selectedPlan.endDate).toLocaleDateString()}</span>
            </div>
            <div className="detail-actions">
              <button onClick={() => setIsEditing(true)}>Edit</button>
              <button onClick={() => setSelectedPlan(null)}>Back</button>
              <button 
                className="delete"
                onClick={() => handleDelete(selectedPlan.id)}
              >
                Delete
              </button>
            </div>
          </div>
        ) : (
          <div className="plan-list">
            {plans.map(plan => (
              <div 
                key={plan.id} 
                className="plan-card"
                onClick={() => setSelectedPlan(plan)}
              >
                <h3>{plan.title}</h3>
                <div className={`status ${plan.status.toLowerCase()}`}>
                  {plan.status.replace('_', ' ')}
                </div>
                <div className="dates">
                  <span>{new Date(plan.startDate).toLocaleDateString()}</span>
                  <span>{new Date(plan.endDate).toLocaleDateString()}</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default LearningPlan;
