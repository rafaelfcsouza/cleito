const axios = require('axios')
const crypto = require('crypto')

const hash = (str) => {
  return crypto.createHash('sha256')
    .update(str)
    .digest('hex');
};

const findProof = ({data: {work: work, difficulty: difficulty}}) => {
  let proof = '';
  let nonce = 0;
  while (!proof.startsWith("0".repeat(difficulty))) {
    nonce++;
    proof = hash(work + nonce);
  }
  return { work: work, proof: proof, nonce: nonce};
};

const testProof = ({ work: work, proof: proof, nonce: nonce}) => {
  return axios.post(`http://localhost:3000/api/pow/work/${work}/proof`, {proof: proof, nonce: nonce});
};

axios.get('http://localhost:3000/api/pow/work')
.then(findProof)
.then(testProof)
.then(r => {
  console.log(r.status)
})
.catch(error => {
  console.log(error);
});