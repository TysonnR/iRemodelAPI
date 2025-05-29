let selectedJobType = null;
let zipCode = null;

// Initialize the page
document.addEventListener('DOMContentLoaded', function() {
    initializeJobTypeSelection();
    initializeLocationInput();
    initializeSearch();
});

function initializeJobTypeSelection() {
    const jobTypeCards = document.querySelectorAll('.job-type-card');

    jobTypeCards.forEach(card => {
        card.addEventListener('click', function() {
            // Remove selection from all cards
            jobTypeCards.forEach(c => c.classList.remove('selected'));

            // Select this card
            this.classList.add('selected');
            selectedJobType = this.dataset.category;

            updateSearchButton();
        });
    });
}

function initializeLocationInput() {
    const zipInput = document.getElementById('zipCodeInput');

    zipInput.addEventListener('input', function() {
        // Only allow numbers and limit to 5 digits
        this.value = this.value.replace(/\D/g, '').substring(0, 5);
        zipCode = this.value.length === 5 ? this.value : null;
        updateSearchButton();
    });

    zipInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && selectedJobType && zipCode) {
            searchContractors();
        }
    });
}

function initializeSearch() {
    const searchButton = document.getElementById('searchButton');
    searchButton.addEventListener('click', searchContractors);
}

function updateSearchButton() {
    const searchButton = document.getElementById('searchButton');
    const canSearch = selectedJobType && zipCode;

    searchButton.disabled = !canSearch;
    searchButton.style.opacity = canSearch ? '1' : '0.6';
}

async function searchContractors() {
    if (!selectedJobType || !zipCode) return;

    // Show results container
    const resultsContainer = document.getElementById('resultsContainer');
    const resultsTitle = document.getElementById('resultsTitle');
    const resultsSubtitle = document.getElementById('resultsSubtitle');
    const resultsContent = document.getElementById('resultsContent');

    resultsContainer.classList.add('active');
    resultsTitle.textContent = 'Finding Perfect Matches...';
    resultsSubtitle.textContent = `Searching for ${selectedJobType.toLowerCase()} contractors in ${zipCode}`;
    resultsContent.innerHTML = '<div class="loading">Analyzing contractors with our intelligent matching algorithm...' +
        '' + '</div>';

    // Scroll to results
    resultsContainer.scrollIntoView({ behavior: 'smooth' });

    try {
        // Simulate API call delay
        await new Promise(resolve => setTimeout(resolve, 2000));

        // Get demo matches based on selection
        const matches = getDemoMatches(selectedJobType, zipCode);

        if (matches.length > 0) {
            displayResults(matches);
        } else {
            displayNoResults();
        }
    } catch (error) {
        console.error('Error searching contractors:', error);
        displayError();
    }
}

function getDemoMatches(jobType, location) {
    // Demo contractor matches based on job type and location
    const allContractors = {
        'REMODELING': [
            {
                name: "Kitchen Pros LLC",
                specialty: "Kitchen Remodeling",
                zipCode: "12345",
                rating: 4.8,
                matchScore: 95,
                reasons: ["Specializes in kitchen remodels", "Located in your area", "Highly rated with 4.8 stars", "15+ years experience"]
            },
            {
                name: "Home Transform Co",
                specialty: "Full Home Remodeling",
                zipCode: "12347",
                rating: 4.6,
                matchScore: 82,
                reasons: ["Expert in full home remodels", "Located nearby", "Excellent customer reviews"]
            }
        ],
        'PLUMBING': [
            {
                name: "Bath Design Solutions",
                specialty: "Bathroom Renovation",
                zipCode: "12346",
                rating: 4.6,
                matchScore: 91,
                reasons: ["Bathroom renovation specialist", "Located nearby", "Licensed and insured", "Beautiful portfolio"]
            },
            {
                name: "AquaFlow Plumbing",
                specialty: "Plumbing Services",
                zipCode: "12345",
                rating: 4.4,
                matchScore: 78,
                reasons: ["Licensed plumber", "Same area", "Emergency services available"]
            }
        ],
        'ROOFING': [
            {
                name: "Peak Roofing Services",
                specialty: "Roofing Installation",
                zipCode: "12345",
                rating: 4.9,
                matchScore: 98,
                reasons: ["Roofing specialist", "Located in your area", "Outstanding 4.9 rating", "20+ years experience", "Insurance approved"]
            }
        ],
        'FLOORING': [
            {
                name: "FloorMaster Installation",
                specialty: "Hardwood Flooring",
                zipCode: "12346",
                rating: 4.7,
                matchScore: 89,
                reasons: ["Flooring expert", "Located nearby", "Premium materials", "Lifetime warranty"]
            }
        ],
        'ELECTRICAL': [
            {
                name: "Spark Electric Solutions",
                specialty: "Electrical Services",
                zipCode: "12350",
                rating: 4.5,
                matchScore: 85,
                reasons: ["Licensed electrician", "Same region", "24/7 emergency service", "Code compliant work"]
            }
        ],
        'PAINTING': [
            {
                name: "ColorCraft Painters",
                specialty: "Interior & Exterior Painting",
                zipCode: "12345",
                rating: 4.3,
                matchScore: 80,
                reasons: ["Professional painters", "Located in your area", "Quality materials", "Color consultation"]
            }
        ],
        'HVAC': [
            {
                name: "Climate Control Pros",
                specialty: "HVAC Installation",
                zipCode: "12346",
                rating: 4.6,
                matchScore: 87,
                reasons: ["HVAC specialists", "Located nearby", "Energy efficient systems", "Maintenance plans"]
            }
        ],
        'GENERAL': [
            {
                name: "Build Better Contracting",
                specialty: "General Construction",
                zipCode: "12345",
                rating: 4.4,
                matchScore: 83,
                reasons: ["Full-service contractor", "Located in your area", "Licensed and bonded", "Project management"]
            }
        ]
    };

    // Return contractors for selected job type, or empty array if none
    return allContractors[jobType] || [];
}

function displayResults(matches) {
    const resultsTitle = document.getElementById('resultsTitle');
    const resultsSubtitle = document.getElementById('resultsSubtitle');
    const resultsContent = document.getElementById('resultsContent');

    resultsTitle.textContent = `✨ Found ${matches.length} Perfect Match${matches.length > 1 ? 'es' : ''}`;
    resultsSubtitle.textContent = `Top-rated ${selectedJobType.toLowerCase()} contractors in your area`;

    const contractorGrid = document.createElement('div');
    contractorGrid.className = 'contractor-grid';

    matches.forEach(contractor => {
        const contractorCard = createContractorCard(contractor);
        contractorGrid.appendChild(contractorCard);
    });

    resultsContent.innerHTML = '';
    resultsContent.appendChild(contractorGrid);
}

function createContractorCard(contractor) {
    const div = document.createElement('div');
    div.className = 'contractor-card';

    const stars = '★'.repeat(Math.floor(contractor.rating)) +
        '☆'.repeat(5 - Math.floor(contractor.rating));

    div.innerHTML = `
                <div class="match-score">${contractor.matchScore}</div>
                <div class="contractor-name">${contractor.name}</div>
                <div class="contractor-specialty">${contractor.specialty}</div>
                <div class="contractor-info">
                    <div class="rating">
                        <span class="stars">${stars}</span>
                        <span>${contractor.rating}</span>
                    </div>
                    <div class="location">📍 ${contractor.zipCode}</div>
                </div>
                <div class="match-reasons">
                    <h4>Why this contractor is perfect for you:</h4>
                    ${contractor.reasons.map(reason => `<div class="reason">✓ ${reason}</div>`).join('')}
                </div>
            `;

    return div;
}

function displayNoResults() {
    const resultsTitle = document.getElementById('resultsTitle');
    const resultsSubtitle = document.getElementById('resultsSubtitle');
    const resultsContent = document.getElementById('resultsContent');

    resultsTitle.textContent = 'No Contractors Found';
    resultsSubtitle.textContent = `We couldn't find contractors for ${selectedJobType.toLowerCase()} in ${zipCode}`;
    resultsContent.innerHTML = `
                <div class="no-results">
                    <h3>🔍 No matches found</h3>
                    <p>Try selecting a different job type or expanding your search area.</p>
                </div>
            `;
}

function displayError() {
    const resultsContent = document.getElementById('resultsContent');
    resultsContent.innerHTML = `
                <div class="no-results">
                    <h3>⚠️ Search Error</h3>
                    <p>There was an error finding contractors. Please try again.</p>
                </div>
            `;
}