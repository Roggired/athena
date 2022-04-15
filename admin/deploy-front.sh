cd frontend
npm install
npm run build:dev
cd ..
mkdir src/main/webapp
cp -rf frontend/build/* src/main/webapp
cp -rf src/main/resources/WEB-INF src/main/webapp