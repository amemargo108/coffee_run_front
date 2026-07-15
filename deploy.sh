set -e
cd backend
docker build -t coffeerun-backend:latest .
echo "Docker image built"
cd ..
cd frontend
npm install --silent
npx ng build --configuration production
echo "Angular built"
cd ..
echo "Build complete"